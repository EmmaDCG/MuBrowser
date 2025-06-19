package com.mu.game.model.equip.rune;

import com.mu.game.model.equip.star.StarForgingData;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class RuneForgingData {
   private static HashMap canForgingItemTypes = new HashMap();
   private static HashMap datas = new HashMap();
   private static final int maxCount = 3;
   private static int minStarLevel = 1;
   private int number;
   private int rate;
   private int showRate;
   private int openLevel;
   private int money;

   public RuneForgingData(int number, int rate, int showRate, int openLevel, int money) {
      this.number = number;
      this.rate = rate;
      this.showRate = showRate;
      this.openLevel = openLevel;
      this.money = money;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();
      String cfTypeStr = Tools.getCellValue(sheet.getCell("A2"));
      String[] splits = cfTypeStr.split(",");
      String[] var7 = splits;
      int openLevel = splits.length;

      int number;
      for(number = 0; number < openLevel; ++number) {
         String s = var7[number];
         Integer itemType = Integer.parseInt(s);
         canForgingItemTypes.put(itemType, Integer.valueOf(1));
      }

      int i;
      for(i = 4; i <= rows; ++i) {
         number = Tools.getCellIntValue(sheet.getCell("A" + i));
         openLevel = Tools.getCellIntValue(sheet.getCell("B" + i));
         int rate = Tools.getCellIntValue(sheet.getCell("C" + i));
         int money = Tools.getCellIntValue(sheet.getCell("D" + i));
         int showRate = Tools.getCellIntValue(sheet.getCell("E" + i));
         if (openLevel > StarForgingData.getCommonMaxStraLevel()) {
            throw new Exception("符文镶嵌 - 开启的星级大于最大星级");
         }

         RuneForgingData data = new RuneForgingData(number, rate, showRate, openLevel, money);
         datas.put(number, data);
      }

      for(i = 1; i <= 3; ++i) {
         if (datas.get(i) == null) {
            throw new Exception("符文镶嵌 - 数据不完整，缺少第" + i + "级");
         }
      }

      minStarLevel = needStarLevel(1);
   }

   public static RuneForgingData getData(int number) {
      return (RuneForgingData)datas.get(number);
   }

   public static boolean canForging(int itemType) {
      return canForgingItemTypes.containsKey(itemType);
   }

   public static int getItemMaxRuneCount(int starLevel) {
      for(int i = 3; i >= 1; --i) {
         RuneForgingData data = getData(i);
         if (data.getOpenLevel() <= starLevel) {
            return i;
         }
      }

      return 0;
   }

   public static int needStarLevel(int number) {
      return number >= 1 && number <= 3 ? ((RuneForgingData)datas.get(number)).getOpenLevel() : StarForgingData.getCommonMaxStraLevel() + 1;
   }

   public static int getMaxcount() {
      return 3;
   }

   public static int getMinStarLevel() {
      return minStarLevel;
   }

   public int getNumber() {
      return this.number;
   }

   public void setNumber(int number) {
      this.number = number;
   }

   public int getRate() {
      return this.rate;
   }

   public void setRate(int rate) {
      this.rate = rate;
   }

   public int getShowRate() {
      return this.showRate;
   }

   public void setShowRate(int showRate) {
      this.showRate = showRate;
   }

   public int getOpenLevel() {
      return this.openLevel;
   }

   public void setOpenLevel(int openLevel) {
      this.openLevel = openLevel;
   }

   public int getMoney() {
      return this.money;
   }

   public void setMoney(int money) {
      this.money = money;
   }
}
