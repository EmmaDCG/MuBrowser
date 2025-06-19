package com.mu.game.dungeon.imp.discovery;

import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.model.map.BigMonsterGroup;
import com.mu.game.model.unit.material.MaterialTemplate;
import com.mu.utils.Rnd;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscoveryTemplate extends DungeonTemplate {
   public static final int Discovery_Monster = 1;
   public static final int Discovery_Chest = 2;
   private HashMap infoMap = new HashMap();
   private HashMap monsterMap = new HashMap();
   private ArrayList chestList = new ArrayList();
   private HashMap chestItemMap = new HashMap();
   private int maxRate = 0;
   private Logger logger = LoggerFactory.getLogger(DiscoveryTemplate.class);

   public DiscoveryTemplate(Workbook wb) {
      super(12, wb);
   }

   public void init() throws Exception {
      this.initDiscoveyInfo(this.wb.getSheet(2));
      this.initMonsterStar(this.wb.getSheet(3));
      this.initMonsters(this.wb.getSheet(4));
      this.initChestInfo(this.wb.getSheet(5));
      this.initChestItem(this.wb.getSheet(6));
   }

   private void initMonsters(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int j = 2; j <= rows; ++j) {
         DiscoveyMonsterGroup group = new DiscoveyMonsterGroup();
         BigMonsterGroup.parseMonsterGroup(group, sheet, j);
         group.setBossRank(Tools.getCellIntValue(sheet.getCell("T" + j)) == 1 ? 2 : 0);
         group.setDunType(Tools.getCellIntValue(sheet.getCell("U" + j)));
         group.setDiscoveryLevel(Tools.getCellIntValue(sheet.getCell("V" + j)));
         ArrayList list = (ArrayList)this.monsterMap.get(group.getDunType() + "_" + group.getDiscoveryLevel());
         if (list == null) {
            list = new ArrayList();
            this.monsterMap.put(group.getDunType() + "_" + group.getDiscoveryLevel(), list);
         }

         list.add(group);
         this.checkMonsterStar(group);
      }

   }

   private void initChestInfo(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         String name = Tools.getCellValue(sheet.getCell("A" + i));
         int templateId = Tools.getCellIntValue(sheet.getCell("B" + i));
         int x = Tools.getCellIntValue(sheet.getCell("D" + i));
         int y = Tools.getCellIntValue(sheet.getCell("E" + i));
         ChestInfo ci = new ChestInfo();
         ci.setName(name);
         ci.setTemplateId(templateId);
         ci.setX(x);
         ci.setY(y);
         ci.setModelId(MaterialTemplate.getTemplate(templateId).getModelId());
         this.chestList.add(ci);
      }

   }

   public ArrayList getChestList() {
      return this.chestList;
   }

   private void initChestItem(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int dunType = Tools.getCellIntValue(sheet.getCell("A" + i));
         int discoveryLevel = Tools.getCellIntValue(sheet.getCell("B" + i));
         int boxId = Tools.getCellIntValue(sheet.getCell("C" + i));
         this.chestItemMap.put(dunType + "_" + discoveryLevel, boxId);
      }

   }

   private void initDiscoveyInfo(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      int curRate;
      for(curRate = 2; curRate <= rows; ++curRate) {
         int did = Tools.getCellIntValue(sheet.getCell("A" + curRate));
         int mapId = Tools.getCellIntValue(sheet.getCell("C" + curRate));
         String mapName = Tools.getCellValue(sheet.getCell("D" + curRate));
         int x = Tools.getCellIntValue(sheet.getCell("E" + curRate));
         int y = Tools.getCellIntValue(sheet.getCell("F" + curRate));
         int rate = Tools.getCellIntValue(sheet.getCell("G" + curRate));
         int type = Tools.getCellIntValue(sheet.getCell("H" + curRate));
         String rd = Tools.getCellValue(sheet.getCell("I" + curRate));
         DiscoveryInfo di = new DiscoveryInfo();
         di.setDiscoveyId(did);
         di.setMapId(mapId);
         di.setMapName(mapName);
         di.setSourceRate(rate);
         di.setX(x);
         di.setY(y);
         di.setDunType(type);
         di.setRightDes(rd);
         this.infoMap.put(did, di);
         this.maxRate += rate;
      }

      curRate = 0;

      DiscoveryInfo di;
      for(Iterator it = this.infoMap.values().iterator(); it.hasNext(); curRate = di.getMaxRate()) {
         di = (DiscoveryInfo)it.next();
         di.setMinRate(curRate + 1);
         di.setMaxRate(di.getMinRate() + di.getSourceRate() - 1);
      }

   }

   public int getMaxRate() {
      return this.maxRate;
   }

   public DiscoveryInfo getRndDiscoveryInfo() {
      int tmp = Rnd.get(1, this.maxRate);
      Iterator it = this.infoMap.values().iterator();

      DiscoveryInfo info;
      do {
         if (!it.hasNext()) {
            this.logger.error("get a null dun, rnd number = {},max rate = {}", tmp, this.maxRate);
            return null;
         }

         info = (DiscoveryInfo)it.next();
      } while(tmp < info.getMinRate() || tmp > info.getMaxRate());

      return info;
   }

   public int getBoxId(int discoveryId, int discoveryLevel) {
      return ((Integer)this.chestItemMap.get(discoveryId + "_" + discoveryLevel)).intValue();
   }

   public DiscoveryInfo getDiscoveryInfo(int discoveryId) {
      return (DiscoveryInfo)this.infoMap.get(discoveryId);
   }

   public ArrayList getMonsterList(int discoveryId, int discoveryLevel) {
      return (ArrayList)this.monsterMap.get(discoveryId + "_" + discoveryLevel);
   }

   public boolean showDynamicMenu() {
      return false;
   }
}
