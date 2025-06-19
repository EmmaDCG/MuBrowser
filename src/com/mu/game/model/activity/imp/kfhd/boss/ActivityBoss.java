package com.mu.game.model.activity.imp.kfhd.boss;

import com.mu.config.Global;
import com.mu.db.manager.ActivityDBManager;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import jxl.Sheet;

public class ActivityBoss extends Activity {
   private ConcurrentHashMap killRecord = new ConcurrentHashMap(8, 0.75F, 2);

   public ActivityBoss(int id) {
      super(id);
      this.digitalRelationId = 4;
   }

   public void init(Object obj) throws Exception {
      Sheet sheet = (Sheet)obj;
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int eid = Tools.getCellIntValue(sheet.getCell("B" + i));
         int bossId = Tools.getCellIntValue(sheet.getCell("C" + i));
         String itemStr = Tools.getCellValue(sheet.getCell("D" + i));
         int header = Tools.getCellIntValue(sheet.getCell("E" + i));
         ActivityBossElement ae = new ActivityBossElement(eid, this);
         ae.setNumerical(bossId);
         ae.setHeader(header);
         ae.setRewardList(Tools.parseItemList(itemStr));
         ae.setUnitList(Tools.parseItemDataUnitList(itemStr));
         this.addElement(ae, true);
      }

      Calendar oc = Calendar.getInstance();
      oc.setTime(Global.getOpenServerTiem());
      this.setOpenDate(oc.getTime());
      Calendar cc = Calendar.getInstance();
      cc.setTime(Global.getOpenServerTiem());
      cc.add(11, this.getDuration());
      this.setCloseDate(cc.getTime());
      if (this.isOpen()) {
         ActivityDBManager.initKillBossLog(this);
      }

   }

   public void open() {
      this.killRecord.clear();
      ActivityDBManager.initKillBossLog(this);
      super.open();
   }

   public void addKillRecord(int bossId, long rid) {
      ConcurrentHashMap map = (ConcurrentHashMap)this.killRecord.get(bossId);
      if (map == null) {
         map = new ConcurrentHashMap(8, 0.75F, 2);
         this.killRecord.put(bossId, map);
      }

      if (!map.containsKey(rid)) {
         map.put(rid, true);
      }

   }

   public boolean hasKilled(int bossId, long rid) {
      ConcurrentHashMap map = (ConcurrentHashMap)this.killRecord.get(bossId);
      return map == null ? false : map.containsKey(rid);
   }

   public int getActivityType() {
      return 2;
   }

   public void writeDetail(Player player) {
      ActivityInfo ai = new ActivityInfo();

      try {
         ai.writeByte(this.getId());
         ai.writeUTF(this.getCloseTimeStr());
         ArrayList al = this.getElementList();
         ai.writeByte(al.size());
         Iterator var5 = al.iterator();

         while(var5.hasNext()) {
            ActivityElement element = (ActivityElement)var5.next();
            ActivityBossElement ae = (ActivityBossElement)element;
            ae.writeDetail(player, ai);
         }

         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public boolean isOpen() {
      return Global.isInterServiceServer() ? false : super.isOpen();
   }

   public int getShellId() {
      return 2;
   }
}
