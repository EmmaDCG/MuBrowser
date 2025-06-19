package com.mu.game.dungeon.imp.temple;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.model.item.Item;
import com.mu.game.model.mall.ShortcutBuyPanel;
import com.mu.game.model.map.BigMonsterGroup;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dungeon.RequestDungeonInfo;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.mall.OpenShortcutBuyAndUse;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import jxl.Sheet;
import jxl.Workbook;

public class TempleTemplate extends DungeonTemplate {
   private HashMap templeMap = new HashMap();
   private HashMap levelMap = new HashMap();

   public TempleTemplate(Workbook wb) {
      super(4, wb);
   }

   public void init() throws Exception {
      this.initTempleLevel(this.wb.getSheet(2));
      this.initMonsterStar(this.wb.getSheet(3));
      this.initMonsters(this.wb.getSheet(4));
      this.initBoss(this.wb.getSheet(5));
      this.createTemple();
   }

   private void initTempleLevel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         TempleLevel tl = new TempleLevel();
         this.initDungeonLevel(sheet, i, tl);
         tl.setPanelDes(Tools.getCellValue(sheet.getCell("I2")));
         tl.setShortcutBuyTitle(Tools.getCellValue(sheet.getCell("K2")));
         String buyStr = Tools.getCellValue(sheet.getCell("J2"));
         if (buyStr != null && !buyStr.trim().equals("")) {
            String[] tmp = buyStr.trim().split(";");

            for(int j = 0; j < tmp.length; ++j) {
               String[] iStr = tmp[j].split(",");
               int modelId = Integer.parseInt(iStr[0]);
               int price = Integer.parseInt(iStr[1]);
               boolean isBind = Integer.parseInt(iStr[2]) == 1;
               int moneyType = Integer.parseInt(iStr[3]);
               Item item = ShortcutBuyPanel.addSellItem("卡利玛快捷购买 ", modelId, price, moneyType, isBind, -1L);
               tl.addBuyItem(j, item);
            }
         }

         this.levelMap.put(tl.getLevel(), tl);
      }

   }

   private void initMonsters(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int j = 2; j <= rows; ++j) {
         TempleMonsterGroup group = new TempleMonsterGroup();
         BigMonsterGroup.parseMonsterGroup(group, sheet, j);
         int templeLevel = Tools.getCellIntValue(sheet.getCell("T" + j));
         TempleLevel tl = (TempleLevel)this.levelMap.get(templeLevel);
         if (tl != null) {
            tl.addMonster(group);
            this.checkMonsterStar(group);
         }
      }

   }

   private void initBoss(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int j = 2; j <= rows; ++j) {
         TempleMonsterGroup group = new TempleMonsterGroup();
         BigMonsterGroup.parseMonsterGroup(group, sheet, j);
         group.setBossId(Tools.getCellIntValue(sheet.getCell("U" + j)));
         group.setHeader(Tools.getCellIntValue(sheet.getCell("T" + j)));
         int templeLevel = Tools.getCellIntValue(sheet.getCell("V" + j));
         TempleLevel tl = (TempleLevel)this.levelMap.get(templeLevel);
         if (tl != null) {
            tl.addBoss(group);
            this.checkMonsterStar(group);
         }
      }

   }

   public TempleLevel getFitLevel(Player player) {
      Iterator it = this.levelMap.values().iterator();

      TempleLevel tl;
      do {
         if (!it.hasNext()) {
            return (TempleLevel)this.levelMap.get(Integer.valueOf(1));
         }

         tl = (TempleLevel)it.next();
      } while(tl.getMinLevelReq() > player.getLevel() || tl.getMaxLevelReq() < player.getLevel());

      return tl;
   }

   public void writeDungeonInfo(Player player) throws Exception {
      TempleLevel tl = this.getFitLevel(player);
      RequestDungeonInfo packet = new RequestDungeonInfo();
      packet.writeByte(this.getTemplateID());
      packet.writeUTF(tl.getRewardStr());
      ArrayList dropItem = tl.getDropList();
      packet.writeByte(dropItem.size());
      Iterator it = dropItem.iterator();

      while(it.hasNext()) {
         Item item = (Item)it.next();
         GetItemStats.writeItem(item, packet);
      }

      GetItemStats.writeItem(tl.getReqItem(), packet);
      HashMap map = this.getTemple(tl.getLevel()).getTempleMap().getBossMap();
      packet.writeByte(map.size());
      it = map.values().iterator();

      while(it.hasNext()) {
         TempleMonster monster = (TempleMonster)it.next();
         packet.writeShort(monster.getLevel());
         packet.writeUTF(monster.getName());
         packet.writeShort(monster.getHeader());
         packet.writeInt(monster.getNextRevivalTime() - 1);
         packet.writeInt(monster.getBornPoint().x);
         packet.writeInt(monster.getBornPoint().y);
      }

      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public void createTemple() {
      Iterator it = this.levelMap.values().iterator();

      while(it.hasNext()) {
         TempleLevel tl = (TempleLevel)it.next();
         Temple tp = new Temple(DungeonManager.getID(), this, tl);
         tp.init();
         this.templeMap.put(tl.getLevel(), tp);
      }

   }

   public void writeShortcutBuyTicket(Player player, int key) {
      try {
         TempleLevel tl = this.getFitLevel(player);
         if (tl == null) {
            return;
         }

         OpenShortcutBuyAndUse osbu = new OpenShortcutBuyAndUse();
         osbu.writeInt(key);
         osbu.writeUTF(tl.getShortcutBuyTitle());
         TreeMap map = tl.getBuyItemMap();
         osbu.writeByte(map.size());
         Iterator it = map.values().iterator();

         while(it.hasNext()) {
            GetItemStats.writeItem((Item)it.next(), osbu);
         }

         player.writePacket(osbu);
         osbu.destroy();
         osbu = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public boolean showDynamicMenu() {
      return true;
   }

   public Temple getTemple(int level) {
      return (Temple)this.templeMap.get(level);
   }
}
