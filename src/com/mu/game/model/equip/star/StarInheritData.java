package com.mu.game.model.equip.star;

import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class StarInheritData {
   private static int minStar = 5;
   private static HashMap datas = new HashMap();
   private static HashMap canForgingItemTypes = new HashMap();
   private int starLevel;
   private int money;
   private int moneyType;

   public StarInheritData(int starLevel, int money) {
      this.starLevel = starLevel;
      this.money = money;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();
      String cfTypes = Tools.getCellValue(sheet.getCell("A2"));
      String[] typeSplits = cfTypes.split(",");
      String[] var7 = typeSplits;
      int money = typeSplits.length;

      int starLevel;
      for(starLevel = 0; starLevel < money; ++starLevel) {
         String s = var7[starLevel];
         canForgingItemTypes.put(Integer.parseInt(s), Integer.valueOf(1));
      }

      int i;
      for(i = 4; i <= rows; ++i) {
         starLevel = Tools.getCellIntValue(sheet.getCell("A" + i));
         money = Tools.getCellIntValue(sheet.getCell("B" + i));
         int moneyType = Tools.getCellIntValue(sheet.getCell("C" + i));
         if (moneyType != 1 && moneyType != 2) {
            throw new Exception("装备转移数据 - 金钱类型出错  " + starLevel);
         }

         if (minStar > starLevel) {
            minStar = starLevel;
         }

         StarInheritData data = new StarInheritData(starLevel, money);
         data.setMoneyType(moneyType);
         datas.put(starLevel, data);
      }

      for(i = minStar; i <= StarForgingData.getCommonMaxStraLevel(); ++i) {
         if (!hasData(i)) {
            throw new Exception("锻造-装备转移数值不完整");
         }
      }

   }

   public static boolean hasData(int starLevel) {
      return datas.containsKey(starLevel);
   }

   public static StarInheritData getData(int starLevel) {
      return (StarInheritData)datas.get(starLevel);
   }

   public static boolean canInherit(int itemType) {
      return canForgingItemTypes.containsKey(itemType);
   }

   public int getStarLevel() {
      return this.starLevel;
   }

   public void setStarLevel(int starLevel) {
      this.starLevel = starLevel;
   }

   public int getMoneyType() {
      return this.moneyType;
   }

   public void setMoneyType(int moneyType) {
      this.moneyType = moneyType;
   }

   public int getMoney() {
      return this.money;
   }

   public void setMoney(int money) {
      this.money = money;
   }

   public static int getMinStar() {
      return minStar;
   }
}
