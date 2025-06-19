package com.mu.game.model.unit.player.hang;

import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.List;
import jxl.Sheet;

public class SaleCondition {
   private static List starLevelCondition = new ArrayList();
   private static List zhuijiaLevelCondition = new ArrayList();
   private static int starLevelDefaultIndex = 0;
   private static int zhuijiaDefaultIndex = 0;

   public static void initStarLevel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();
      starLevelDefaultIndex = Tools.getCellIntValue(sheet.getCell("B1"));

      for(int i = 3; i <= rows; ++i) {
         int starLevel = Tools.getCellIntValue(sheet.getCell("A" + i));
         String des = Tools.getCellValue(sheet.getCell("B" + i));
         if (starLevel < 1) {
            throw new Exception(sheet.getName() + "第 " + i + " 行,强化等级不正确");
         }

         String[] sc = new String[]{String.valueOf(starLevel), des};
         starLevelCondition.add(sc);
      }

      if (starLevelDefaultIndex < 0 && starLevelDefaultIndex >= starLevelCondition.size()) {
         throw new Exception(sheet.getName() + ",默认数值填写错误");
      }
   }

   public static void initZhuijiaLevel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();
      zhuijiaDefaultIndex = Tools.getCellIntValue(sheet.getCell("B1"));

      for(int i = 3; i <= rows; ++i) {
         int zhuijiaLevel = Tools.getCellIntValue(sheet.getCell("A" + i));
         String des = Tools.getCellValue(sheet.getCell("B" + i));
         if (zhuijiaLevel < 1) {
            throw new Exception(sheet.getName() + "第 " + i + " 行,追加等级不正确");
         }

         String[] sc = new String[]{String.valueOf(zhuijiaLevel), des};
         zhuijiaLevelCondition.add(sc);
      }

      if (zhuijiaDefaultIndex < 0 && zhuijiaDefaultIndex >= starLevelCondition.size()) {
         throw new Exception(sheet.getName() + ",默认数值填写错误");
      }
   }

   public static int getStarLevel(int starIndex) {
      starIndex = correctStarIndex(starIndex);
      return Integer.parseInt(((String[])starLevelCondition.get(starIndex))[0]);
   }

   public static int correctStarIndex(int starIndex) {
      if (starIndex >= starLevelCondition.size()) {
         starIndex = starLevelDefaultIndex;
      }

      starIndex = Math.max(0, starIndex);
      return starIndex;
   }

   public static int getZhuijiaLevel(int zhuijiaIndex) {
      zhuijiaIndex = correctZhuijiaIndex(zhuijiaIndex);
      return Integer.parseInt(((String[])zhuijiaLevelCondition.get(zhuijiaIndex))[0]);
   }

   public static int correctZhuijiaIndex(int zhuijiaIndex) {
      if (zhuijiaIndex >= zhuijiaLevelCondition.size()) {
         zhuijiaIndex = zhuijiaDefaultIndex;
      }

      zhuijiaIndex = Math.max(0, zhuijiaIndex);
      return zhuijiaIndex;
   }

   public static List getStarLevelCondition() {
      return starLevelCondition;
   }

   public static List getZhuijiaLevelCondition() {
      return zhuijiaLevelCondition;
   }

   public static int getStarLevelDefaultIndex() {
      return starLevelDefaultIndex;
   }

   public static int getZhuijiaDefaultIndex() {
      return zhuijiaDefaultIndex;
   }
}
