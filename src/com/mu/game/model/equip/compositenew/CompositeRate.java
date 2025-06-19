package com.mu.game.model.equip.compositenew;

import com.mu.game.model.item.Item;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class CompositeRate {
   private static HashMap levelRateMap = new HashMap();
   private static HashMap zhuijiaRateMap = new HashMap();
   private static HashMap starRateMap = new HashMap();
   private static HashMap setRateMap = new HashMap();
   private static HashMap luckyRateMap = new HashMap();
   private static HashMap excellentRateMap = new HashMap();

   public static void initLevelRate(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int rateType = Tools.getCellIntValue(sheet.getCell("A" + i));
         int level = Tools.getCellIntValue(sheet.getCell("B" + i));
         int rate = Tools.getCellIntValue(sheet.getCell("C" + i));
         if (rate < 1) {
            throw new Exception(sheet.getName() + "--概率填写错误，第" + i + "行");
         }

         addData(levelRateMap, rateType, level, rate);
      }

   }

   public static void initStarRate(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int rateType = Tools.getCellIntValue(sheet.getCell("A" + i));
         int star = Tools.getCellIntValue(sheet.getCell("B" + i));
         int rate = Tools.getCellIntValue(sheet.getCell("C" + i));
         if (rate < 1) {
            throw new Exception(sheet.getName() + "--概率填写错误，第" + i + "行");
         }

         addData(starRateMap, rateType, star, rate);
      }

   }

   public static void initZhuijiaRate(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int rateType = Tools.getCellIntValue(sheet.getCell("A" + i));
         int zhuijiaLevel = Tools.getCellIntValue(sheet.getCell("B" + i));
         int rate = Tools.getCellIntValue(sheet.getCell("C" + i));
         if (rate < 1) {
            throw new Exception(sheet.getName() + "--概率填写错误，第" + i + "行");
         }

         addData(zhuijiaRateMap, rateType, zhuijiaLevel, rate);
      }

   }

   public static void initExcellentRate(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int rateType = Tools.getCellIntValue(sheet.getCell("A" + i));
         int excellent = Tools.getCellIntValue(sheet.getCell("B" + i));
         int rate = Tools.getCellIntValue(sheet.getCell("C" + i));
         if (rate < 1) {
            throw new Exception(sheet.getName() + "--概率填写错误，第" + i + "行");
         }

         addData(excellentRateMap, rateType, excellent, rate);
      }

   }

   public static void initLuckyRate(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int rateType = Tools.getCellIntValue(sheet.getCell("A" + i));
         int rate = Tools.getCellIntValue(sheet.getCell("B" + i));
         if (rate < 1) {
            throw new Exception(sheet.getName() + "--概率填写错误，第" + i + "行");
         }

         luckyRateMap.put(rateType, rate);
      }

   }

   public static void initSetRate(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int rateType = Tools.getCellIntValue(sheet.getCell("A" + i));
         int rate = Tools.getCellIntValue(sheet.getCell("B" + i));
         if (rate < 1) {
            throw new Exception(sheet.getName() + "--概率填写错误，第" + i + "行");
         }

         setRateMap.put(rateType, rate);
      }

   }

   private static void addData(HashMap dataMap, int type, int key, int rate) {
      HashMap map = (HashMap)dataMap.get(type);
      if (map == null) {
         map = new HashMap();
         dataMap.put(type, map);
      }

      map.put(key, rate);
   }

   private static int getLevelRate(int rateType, int level) {
      if (!levelRateMap.containsKey(rateType)) {
         return 0;
      } else {
         Integer rate = (Integer)((HashMap)levelRateMap.get(rateType)).get(level);
         return rate == null ? 0 : rate.intValue();
      }
   }

   private static int getZhuijiaRate(int rateType, int zhuijiaLevel) {
      if (!zhuijiaRateMap.containsKey(rateType)) {
         return 0;
      } else {
         Integer rate = (Integer)((HashMap)zhuijiaRateMap.get(rateType)).get(zhuijiaLevel);
         return rate == null ? 0 : rate.intValue();
      }
   }

   private static int getExcellentRate(int rateType, int excellentCount) {
      if (!excellentRateMap.containsKey(rateType)) {
         return 0;
      } else {
         Integer rate = (Integer)((HashMap)excellentRateMap.get(rateType)).get(excellentCount);
         return rate == null ? 0 : rate.intValue();
      }
   }

   private static int getStarRate(int rateType, int starLevel) {
      if (!starRateMap.containsKey(rateType)) {
         return 0;
      } else {
         Integer rate = (Integer)((HashMap)starRateMap.get(rateType)).get(starLevel);
         return rate == null ? 0 : rate.intValue();
      }
   }

   private static int getSetRate(int rateType) {
      Integer rate = (Integer)setRateMap.get(rateType);
      return rate == null ? 0 : rate.intValue();
   }

   private static int getLuckyRate(int rateType) {
      Integer rate = (Integer)luckyRateMap.get(rateType);
      return rate == null ? 0 : rate.intValue();
   }

   public static int getRate(Item item, int rateType) {
      int rate = getLevelRate(rateType, item.getLevel()) + getStarRate(rateType, item.getStarLevel()) + getZhuijiaRate(rateType, item.getZhuijiaLevel()) + getExcellentRate(rateType, item.getBonusStatSize(3));
      if (item.getModel().getSort() == 1 && item.getModel().getSets() != -1) {
         rate += getSetRate(rateType);
      }

      if (item.getBonusStatSize(2) > 0) {
         rate += getLuckyRate(rateType);
      }

      return rate;
   }
}
