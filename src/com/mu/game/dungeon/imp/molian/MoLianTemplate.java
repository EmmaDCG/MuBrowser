package com.mu.game.dungeon.imp.molian;

import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.map.BigMonsterGroup;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.dun.DunLogs;
import com.mu.io.game.packet.imp.dungeon.RequestDungeonInfo;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class MoLianTemplate extends DungeonTemplate {
   private HashMap levelMap = new HashMap();
   private MoLianManager manager = new MoLianManager(this);

   public MoLianTemplate(Workbook wb) {
      super(11, wb);
      this.canInspire = true;
   }

   public void init() throws Exception {
      this.initGeneral(this.wb.getSheet(1));
      this.initMoLianLevel(this.wb.getSheet(2));
      this.initMonsterStar(this.wb.getSheet(3));
      this.initMonsters(4);
   }

   private void initGeneral(Sheet sheet) {
      this.setInspireMoney(Tools.getCellIntValue(sheet.getCell("I2")));
      this.setInspireIngot(Tools.getCellIntValue(sheet.getCell("J2")));
      this.setMoneyInspireDes(Tools.getCellValue(sheet.getCell("K2")));
      this.setIngotInspireDes(Tools.getCellValue(sheet.getCell("L2")));
   }

   public MoLianleLevel getPlayerFitLevel(Player player) {
      Iterator it = this.levelMap.values().iterator();

      MoLianleLevel dl;
      do {
         if (!it.hasNext()) {
            return null;
         }

         dl = (MoLianleLevel)it.next();
      } while(dl.getMinLevelReq() > player.getLevel() || dl.getMaxLevelReq() < player.getLevel());

      return dl;
   }

   public HashMap getLevelMap() {
      return this.levelMap;
   }

   public boolean showDynamicMenu() {
      return false;
   }

   public void writeDungeonInfo(Player player) throws Exception {
      RequestDungeonInfo packet = new RequestDungeonInfo();
      packet.writeByte(this.getTemplateID());
      DunLogs log = player.getDunLogsManager().getLog(this.templateID);
      int finishiTimes = log == null ? 0 : log.getFinishTimes();
      packet.writeByte(this.getMaxTimes() - finishiTimes);
      packet.writeByte(this.levelMap.size());
      Iterator it = this.levelMap.values().iterator();

      while(it.hasNext()) {
         MoLianleLevel ml = (MoLianleLevel)it.next();
         packet.writeByte(ml.getLevel());
         packet.writeUTF(ml.getName());
         packet.writeShort(ml.getMinLevelReq());
         packet.writeShort(ml.getMaxLevelReq());
         packet.writeInt(ml.getContribution());
         ArrayList dropList = ml.getDropList();
         packet.writeByte(dropList.size());
         Iterator var9 = dropList.iterator();

         while(var9.hasNext()) {
            Item item = (Item)var9.next();
            GetItemStats.writeItem(item, packet);
         }
      }

      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   private void initMoLianLevel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         MoLianleLevel ml = new MoLianleLevel();
         int level = Tools.getCellIntValue(sheet.getCell("A" + i));
         int minLevelReq = Tools.getCellIntValue(sheet.getCell("B" + i));
         int maxLevelReq = Tools.getCellIntValue(sheet.getCell("C" + i));
         String levelDes = Tools.getCellValue(sheet.getCell("D" + i));
         int con = Tools.getCellIntValue(sheet.getCell("E" + i));
         String name = Tools.getCellValue(sheet.getCell("F" + i));
         String dropStr = Tools.getCellValue(sheet.getCell("G" + i));
         int mapId = Tools.getCellIntValue(sheet.getCell("H" + i));
         int defaultX = Tools.getCellIntValue(sheet.getCell("I" + i));
         int defaultY = Tools.getCellIntValue(sheet.getCell("J" + i));
         ml.setLevel(level);
         ml.setMinLevelReq(minLevelReq);
         ml.setMaxLevelReq(maxLevelReq);
         ml.setLevelStr(levelDes);
         ml.setContribution(con);
         ml.setName(name);
         ml.setMapId(mapId);
         ml.setDefaultX(defaultX);
         ml.setDefaultY(defaultY);
         String[] tmp;
         int j;
         if (dropStr.indexOf(";") != -1) {
            tmp = dropStr.split(";");

            for(j = 0; j < tmp.length; ++j) {
               String[] itemStr = tmp[j].split(",");
               ItemDataUnit du = new ItemDataUnit(Integer.parseInt(itemStr[0]), 1);
               du.setStatRuleID(Integer.parseInt(itemStr[1]));
               du.setHide(true);
               du.setBind(false);
               Item item = ItemTools.createItem(2, du);
               ml.addDropItem(item);
            }
         } else {
            tmp = dropStr.split(",");

            for(j = 0; j < tmp.length; ++j) {
               ml.addDropItem(ItemTools.createItem(Integer.parseInt(tmp[j]), 1, 2));
            }
         }

         this.levelMap.put(level, ml);
      }

   }

   private void initMonsters(int sheetIndex) throws Exception {
      Sheet[] sheets = this.wb.getSheets();

      for(int i = sheetIndex; i < sheets.length; ++i) {
         Sheet sheet = sheets[i];
         int rows = sheet.getRows();

         for(int j = 2; j <= rows; ++j) {
            MoLianMonsterGroup group = new MoLianMonsterGroup();
            BigMonsterGroup.parseMonsterGroup(group, sheet, j);
            group.setLevelId(Tools.getCellIntValue(sheet.getCell("T" + j)));
            MoLianleLevel ml = (MoLianleLevel)this.levelMap.get(group.getLevelId());
            if (ml != null) {
               ml.addMonsterGroup(group);
            }

            this.checkMonsterStar(group);
         }
      }

   }

   public int canEnter(Player player, Item ticket, Object... obj) {
      Gang gang = player.getGang();
      if (gang == null) {
         return 9034;
      } else {
         int reqLevel = ((Integer)obj[0]).intValue();
         MoLianleLevel ml = this.getPlayerFitLevel(player);
         if (ml != null && ml.getLevel() == reqLevel) {
            return player.getContribution() < ml.getContribution() ? 9087 : super.canEnter(player, ticket, obj);
         } else {
            return 9086;
         }
      }
   }

   public MoLianManager getManager() {
      return this.manager;
   }
}
