package com.mu.game.model.spiritOfWar.refine;

import com.mu.game.model.item.Item;
import com.mu.game.model.stats.ItemModify;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;

public class RefineData {
   private static HashMap typeMap = new HashMap();
   private static HashMap levelMap = new HashMap();
   private static HashMap statsMap = new HashMap();
   private static HashMap socketMap = new HashMap();
   private static HashMap starLevelMap = new HashMap();
   private static HashMap zhuijiaMap = new HashMap();
   private static HashMap runeMap = new HashMap();
   private static HashMap excelletCountMap = new HashMap();
   private static HashMap setMap = new HashMap();
   private static HashMap luckyMap = new HashMap();

   public static void initType(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int key = Tools.getCellIntValue(sheet.getCell("A" + i));
         int value = Tools.getCellIntValue(sheet.getCell("B" + i));
         typeMap.put(key, value);
      }

      if (typeMap.size() < 1) {
         throw new Exception("战魂-" + sheet.getName() + "没有数据");
      }
   }

   public static void initLevel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int key = Tools.getCellIntValue(sheet.getCell("A" + i));
         int value = Tools.getCellIntValue(sheet.getCell("B" + i));
         levelMap.put(key, value);
      }

      if (levelMap.size() < 1) {
         throw new Exception("战魂-" + sheet.getName() + "没有数据");
      }
   }

   public static void initStats(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int key = Tools.getCellIntValue(sheet.getCell("A" + i));
         int value = Tools.getCellIntValue(sheet.getCell("B" + i));
         statsMap.put(key, value);
      }

      if (statsMap.size() < 1) {
         throw new Exception("战魂-" + sheet.getName() + "没有数据");
      }
   }

   public static void initSocket(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int key = Tools.getCellIntValue(sheet.getCell("A" + i));
         int value = Tools.getCellIntValue(sheet.getCell("B" + i));
         socketMap.put(key, value);
      }

      if (socketMap.size() < 1) {
         throw new Exception("战魂-" + sheet.getName() + "没有数据");
      }
   }

   public static void initStarLevel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int key = Tools.getCellIntValue(sheet.getCell("A" + i));
         int value = Tools.getCellIntValue(sheet.getCell("B" + i));
         starLevelMap.put(key, value);
      }

      if (starLevelMap.size() < 1) {
         throw new Exception("战魂-" + sheet.getName() + "没有数据");
      }
   }

   public static void initZhuijia(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int key = Tools.getCellIntValue(sheet.getCell("A" + i));
         int value = Tools.getCellIntValue(sheet.getCell("B" + i));
         zhuijiaMap.put(key, value);
      }

      if (zhuijiaMap.size() < 1) {
         throw new Exception("战魂-" + sheet.getName() + "没有数据");
      }
   }

   public static void initRune(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int key = Tools.getCellIntValue(sheet.getCell("A" + i));
         int value = Tools.getCellIntValue(sheet.getCell("B" + i));
         runeMap.put(key, value);
      }

      if (runeMap.size() < 1) {
         throw new Exception("战魂-" + sheet.getName() + "没有数据");
      }
   }

   public static void initExcellentCount(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int key = Tools.getCellIntValue(sheet.getCell("A" + i));
         int value = Tools.getCellIntValue(sheet.getCell("B" + i));
         excelletCountMap.put(key, value);
      }

      if (excelletCountMap.size() < 1) {
         throw new Exception("战魂-" + sheet.getName() + "没有数据");
      }
   }

   public static void initSet(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int key = Tools.getCellIntValue(sheet.getCell("A" + i));
         int value = Tools.getCellIntValue(sheet.getCell("B" + i));
         setMap.put(key, value);
      }

      if (setMap.size() < 1) {
         throw new Exception("战魂-" + sheet.getName() + "没有数据");
      }
   }

   public static void initLucky(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int key = Tools.getCellIntValue(sheet.getCell("A" + i));
         int value = Tools.getCellIntValue(sheet.getCell("B" + i));
         luckyMap.put(key, value);
      }

      if (luckyMap.size() < 1) {
         throw new Exception("战魂-" + sheet.getName() + "没有数据");
      }
   }

   private static int getTypeData(int key) {
      return typeMap.containsKey(key) ? ((Integer)typeMap.get(key)).intValue() : 0;
   }

   private static int getLevelData(int key) {
      return levelMap.containsKey(key) ? ((Integer)levelMap.get(key)).intValue() : 0;
   }

   private static int getStatsData(int key) {
      return statsMap.containsKey(key) ? ((Integer)statsMap.get(key)).intValue() : 0;
   }

   private static int getSocketData(int key) {
      return socketMap.containsKey(key) ? ((Integer)socketMap.get(key)).intValue() : 0;
   }

   private static int getStarLevelData(int key) {
      return starLevelMap.containsKey(key) ? ((Integer)starLevelMap.get(key)).intValue() : 0;
   }

   private static int getZhuijiaData(int key) {
      return zhuijiaMap.containsKey(key) ? ((Integer)zhuijiaMap.get(key)).intValue() : 0;
   }

   private static int getExcellentCountData(int key) {
      return excelletCountMap.containsKey(key) ? ((Integer)excelletCountMap.get(key)).intValue() : 0;
   }

   private static int getSetData(int key) {
      return setMap.containsKey(key) ? ((Integer)setMap.get(key)).intValue() : 0;
   }

   private static int getLuckyData(int key) {
      return luckyMap.containsKey(key) ? ((Integer)luckyMap.get(key)).intValue() : 0;
   }

   public static boolean canRefine(int type) {
      return typeMap.containsKey(type);
   }

   public static int getRefineExp(Item item, int index) {
      int value = getTypeData(item.getItemType());
      if (item.isEquipSet()) {
         value += getSetData(item.getLevel());
      }

      value += getLevelData(item.getLevel());
      value += getSocketData(item.getSocket());
      value += getStarLevelData(item.getStarLevel());
      value += getZhuijiaData(item.getZhuijiaLevel());
      value += getLuckyData(item.getBonusStatSize(2) > 1 ? 1 : 0);
      int excellentCount = 0;
      Iterator var5 = item.getOtherStats().values().iterator();

      while(var5.hasNext()) {
         ItemModify modify = (ItemModify)var5.next();
         if (modify.getBonusType() == 3) {
            value += getStatsData(modify.getStat().getStatId());
            ++excellentCount;
         }
      }

      value += getExcellentCountData(excellentCount);
      return Math.max(0, value);
   }
}
