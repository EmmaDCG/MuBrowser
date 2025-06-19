package com.mu.game.dungeon.imp.personalboss;

import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.mall.ShortcutBuyPanel;
import com.mu.game.model.unit.monster.MonsterStar;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.dun.DunLogManager;
import com.mu.game.model.unit.player.dun.DunLogs;
import com.mu.game.model.vip.PlayerVIPManager;
import com.mu.game.model.vip.effect.VIPEffectType;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.mall.OpenShortcutBuyAndUse;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class PersonalBossTemplate extends DungeonTemplate {
   private HashMap bossInfoMap = new HashMap();
   private ArrayList buyItemList = new ArrayList();
   private String shortcutBuyTitle = "";

   public PersonalBossTemplate(Workbook wb) {
      super(7, wb);
   }

   public void init() throws Exception {
      this.initGeneral(this.wb.getSheet(1));
      this.initBossInfo(this.wb.getSheet(2));
   }

   public MonsterStar getMonsterStar(int star, int level) {
      return MonsterStar.getMonsterStar(star, level);
   }

   private void initGeneral(Sheet sheet) throws Exception {
      this.shortcutBuyTitle = Tools.getCellValue(sheet.getCell("J2"));
      String buyStr = Tools.getCellValue(sheet.getCell("I2"));
      if (buyStr != null && !buyStr.trim().equals("")) {
         String[] tmp = buyStr.trim().split(";");

         for(int j = 0; j < tmp.length; ++j) {
            String[] iStr = tmp[j].split(",");
            int modelId = Integer.parseInt(iStr[0]);
            int price = Integer.parseInt(iStr[1]);
            boolean isBind = Integer.parseInt(iStr[2]) == 1;
            int moneyType = Integer.parseInt(iStr[3]);
            Item item = ShortcutBuyPanel.addSellItem("个人boss快捷购买  ", modelId, price, moneyType, isBind, -1L);
            this.buyItemList.add(item);
         }
      }

   }

   public void writeShortcutBuyTicket(Player player, int key) {
      try {
         OpenShortcutBuyAndUse osbu = new OpenShortcutBuyAndUse();
         osbu.writeInt(key);
         osbu.writeUTF(this.shortcutBuyTitle);
         osbu.writeByte(this.buyItemList.size());
         Iterator var5 = this.buyItemList.iterator();

         while(var5.hasNext()) {
            Item item = (Item)var5.next();
            GetItemStats.writeItem(item, osbu);
         }

         player.writePacket(osbu);
         osbu.destroy();
         osbu = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   private void initBossInfo(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int bossId = Tools.getCellIntValue(sheet.getCell("A" + i));
         int x = Tools.getCellIntValue(sheet.getCell("B" + i));
         int y = Tools.getCellIntValue(sheet.getCell("C" + i));
         int timeLimit = Tools.getCellIntValue(sheet.getCell("D" + i));
         int levelLimit = Tools.getCellIntValue(sheet.getCell("E" + i));
         int ticket = Tools.getCellIntValue(sheet.getCell("F" + i));
         int ticketNumber = Tools.getCellIntValue(sheet.getCell("G" + i));
         BossInfo info = new BossInfo();
         info.setBossId(bossId);
         info.setTimeLimit(timeLimit);
         info.setX(x);
         info.setY(y);
         info.setLevelLimit(levelLimit);
         info.setTicket(ticket);
         info.setTicketNumber(ticketNumber);
         if (ticket != -1) {
            info.setTicketItem(ItemTools.createItem(ticket, ticketNumber, 2));
         }

         this.bossInfoMap.put(bossId, info);
      }

   }

   public BossInfo getBossInfo(int bossId) {
      return (BossInfo)this.bossInfoMap.get(bossId);
   }

   public int getMaxTimes(Player player, int smallId) {
      PlayerVIPManager manager = player.getVIPManager();
      return this.maxTimes + manager.getEffectIntegerValue(VIPEffectType.VE_7);
   }

   public int canEnter(Player player, Item ticket, Object... obj) {
      BossInfo info = (BossInfo)obj[0];
      if (player.getLevel() < info.getLevelLimit()) {
         return 14044;
      } else {
         DunLogManager lm = player.getDunLogsManager();
         DunLogs log = lm.getLog(this.templateID, info.getBossId());
         return log != null && log.getFinishTimes() >= lm.getMaxtimes(this.templateID, info.getBossId()) ? 14011 : super.canEnter(player, ticket, obj);
      }
   }

   public String getCanotEnterMessage(int code, BossInfo info) {
      return super.getCanotEnterMessage(code, info.getTicketItem());
   }

   public boolean showDynamicMenu() {
      return false;
   }
}
