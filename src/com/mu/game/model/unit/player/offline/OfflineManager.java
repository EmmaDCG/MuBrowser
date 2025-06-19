package com.mu.game.model.unit.player.offline;

import com.mu.config.Global;
import com.mu.config.MessageText;
import com.mu.db.log.IngotChangeType;
import com.mu.executor.imp.buff.SaveBuffWhenOffLineExecutor;
import com.mu.executor.imp.dun.SaveRecoverExecutor;
import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.ui.dm.DynamicMenuManager;
import com.mu.game.model.unit.buff.Buff;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.dun.DunLogManager;
import com.mu.game.model.unit.player.dun.DunLogs;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.player.offline.GetOfflineRecoveryInfo;
import com.mu.io.game.packet.imp.sys.OpenPanel;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class OfflineManager {
   public static final long MaxBuffTime = 259200000L;
   private static HashMap infoMap = new HashMap();
   private static HashMap buffMap = new HashMap();
   private static String buffTips = "";
   private static String buffDes = "";
   private HashMap todayRecoverMap = new HashMap();
   private HashMap lastRecoverMap = new HashMap();
   private Player owner = null;
   private long offlineTime = 0L;
   private int offlineHours = 0;
   private int offlineMinutes = 0;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      initRecover(wb.getSheet(1));
      initOfflineBuff(wb.getSheet(2));
      initDes(wb.getSheet(3));
   }

   private static void initRecover(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int dunId = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int maxTimes = Tools.getCellIntValue(sheet.getCell("C" + i));
         int bindIngot = Tools.getCellIntValue(sheet.getCell("D" + i));
         int ingot = Tools.getCellIntValue(sheet.getCell("E" + i));
         int menuId = Tools.getCellIntValue(sheet.getCell("F" + i));
         DunRecoveryInfo info = new DunRecoveryInfo();
         info.setDunId(dunId);
         info.setMaxTimes(maxTimes);
         info.setBindIngot(bindIngot);
         info.setIngot(ingot);
         info.setName(name);
         info.setMenuId(menuId);
         infoMap.put(dunId, info);
      }

   }

   private static void initOfflineBuff(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int vipLevel = Tools.getCellIntValue(sheet.getCell("A" + i));
         int buffId = Tools.getCellIntValue(sheet.getCell("B" + i));
         int buffLevel = Tools.getCellIntValue(sheet.getCell("C" + i));
         int addition = Tools.getCellIntValue(sheet.getCell("D" + i));
         OfflineBuffInfo info = new OfflineBuffInfo();
         info.setAddition(addition);
         info.setBuffId(buffId);
         info.setBuffLevel(buffLevel);
         info.setVipLevel(vipLevel);
         buffMap.put(vipLevel, info);
      }

   }

   private static void initDes(Sheet sheet) throws Exception {
      buffTips = Tools.getCellValue(sheet.getCell("A2"));
      buffDes = Tools.getCellValue(sheet.getCell("B2"));
   }

   public static HashMap getInfoMap() {
      return infoMap;
   }

   public static DunRecoveryInfo getDunRecoveryInfo(int id) {
      return (DunRecoveryInfo)infoMap.get(id);
   }

   public static OfflineBuffInfo getBuffInfo(int vipLevel) {
      return (OfflineBuffInfo)buffMap.get(vipLevel);
   }

   public static String getBuffTips() {
      return buffTips;
   }

   public static String getBuffDes() {
      return buffDes;
   }

   public OfflineManager(Player owner) {
      this.owner = owner;
   }

   public Player getOwner() {
      return this.owner;
   }

   public PlayerDunRecover getLastRecover(int dunId) {
      return (PlayerDunRecover)this.lastRecoverMap.get(dunId);
   }

   public void addLastRecover(PlayerDunRecover recover) {
      this.lastRecoverMap.put(recover.getDunId(), recover);
   }

   public int getRecoverTimes(int dunId) {
      PlayerDunRecover recover = (PlayerDunRecover)this.todayRecoverMap.get(dunId);
      return recover == null ? 0 : recover.getRecoverTimes();
   }

   public PlayerDunRecover getTodayRecover(int dunId) {
      return (PlayerDunRecover)this.todayRecoverMap.get(dunId);
   }

   public void doRecover(int id, int times) {
      DunRecoveryInfo info = getDunRecoveryInfo(id);
      if (info != null) {
         PlayerDunRecover recover = this.getTodayRecover(id);
         if (recover != null && times >= 1 && times <= recover.getRemainderTimes()) {
            boolean success = false;
            int bindIngot = times * info.getBindIngot();
            if (this.getOwner().getBindIngot() >= bindIngot) {
               success = PlayerManager.reduceBindIngot(this.getOwner(), bindIngot, IngotChangeType.Recover, "" + times) == 1;
            } else {
               int ingot = times * info.getIngot();
               if (this.getOwner().getIngot() < ingot) {
                  SystemMessage.writeMessage(this.getOwner(), 14048);
                  return;
               }

               success = PlayerManager.reduceIngot(this.getOwner(), ingot, IngotChangeType.Recover, "" + times) == 1;
            }

            if (success) {
               recover.setRecoverTimes(recover.getRecoverTimes() + times);
               recover.setRemainderTimes(recover.getRemainderTimes() - times);
               SaveRecoverExecutor.saveRecover(this.getOwner(), id, recover.getRecoverTimes(), recover.getRemainderTimes());
               UpdateMenu.update(this.getOwner(), info.getMenuId());
               GetOfflineRecoveryInfo.pushAllRecoveryInfo(this.getOwner());
               SystemMessage.writeMessage(this.getOwner(), MessageText.getText(14051).replace("%s%", info.getName()).replace("%i%", String.valueOf(times)), 14051);
            }

         } else {
            SystemMessage.writeMessage(this.getOwner(), 14047);
         }
      }
   }

   public void addRecover(PlayerDunRecover recover) {
      this.todayRecoverMap.put(recover.getDunId(), recover);
   }

   public int getCanRecoverTimes(int dunId) {
      PlayerDunRecover recover = this.getTodayRecover(dunId);
      return recover == null ? 0 : recover.getRemainderTimes();
   }

   public void dayChange() {
      if (FunctionOpenManager.isOpen(this.getOwner(), 21)) {
         DunLogManager logManager = this.getOwner().getDunLogsManager();
         Iterator it = infoMap.values().iterator();

         while(it.hasNext()) {
            DunRecoveryInfo info = (DunRecoveryInfo)it.next();
            DunLogs log = logManager.getLog(info.getDunId());
            DungeonTemplate template = DungeonTemplateFactory.getTemplate(info.getDunId());
            PlayerDunRecover recover = this.getTodayRecover(info.getDunId());
            if (recover == null) {
               recover = new PlayerDunRecover(info.getDunId());
               this.addRecover(recover);
            }

            recover.setRecoverDay(Time.getDayLong());
            recover.setRecoverTimes(0);
            int finishTimes = log == null ? 0 : log.getFinishTimes();
            int tmp = template.getMaxTimes() - finishTimes;
            if (tmp > 0) {
               recover.setRemainderTimes(recover.getRemainderTimes() + tmp);
               if (recover.getRemainderTimes() > info.getMaxTimes()) {
                  recover.setRemainderTimes(info.getMaxTimes());
               }
            }
         }

      }
   }

   public void initOfflineBuff(long time) {
      this.offlineTime = time < 0L ? 0L : time;
      if (time > 259200000L) {
         time = 259200000L;
      }

      this.offlineHours = (int)(time / 1000L / 3600L);
      this.offlineMinutes = (int)((time - (long)(this.offlineHours * 3600) * 1000L) / 1000L / 60L);
      if (time > 60000L) {
         long duration = time;
         OfflineBuffInfo info = getBuffInfo(this.getOwner().getVipShowLevel());
         Buff buff = this.getOwner().getBuffManager().getBuff(info.getBuffId());
         if (buff != null) {
            duration = time + SaveBuffWhenOffLineExecutor.getRemainTime(buff, System.currentTimeMillis());
         }

         if (duration > 259200000L) {
            duration = 259200000L;
         }

         this.getOwner().getBuffManager().createAndStartBuff(this.getOwner(), info.getBuffId(), info.getBuffLevel(), true, duration);
         if (time > 600000L && Global.isPushOffline()) {
            int[] uiId = DynamicMenuManager.getMenu(30).getPanelId();
            OpenPanel.open(this.getOwner(), uiId[0], uiId[1]);
         }
      }

   }

   public String getOfflineTimeStr() {
      return MessageText.getText(13051).replace("%h%", String.valueOf(this.offlineHours)).replace("%m%", String.valueOf(this.offlineMinutes));
   }

   public long getOfflineTime() {
      return this.offlineTime;
   }

   public void destroy() {
      if (this.todayRecoverMap != null) {
         this.todayRecoverMap.clear();
         this.todayRecoverMap = null;
      }

      if (this.lastRecoverMap != null) {
         this.lastRecoverMap.clear();
         this.lastRecoverMap = null;
      }

      this.owner = null;
   }
}
