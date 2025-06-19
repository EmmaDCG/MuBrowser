package com.mu.game.model.hallow.model;

import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class HallowModel {
   public static int MaxRank = 1;
   private static HashMap modelMap = new HashMap();
   private int rank;
   private String name;
   private int maxLevel;
   private int treasureLevel;
   private int scale = 100;
   private int view3D;

   public HallowModel(int rank, String name, int maxLevel, int treasureLevel) {
      this.rank = rank;
      this.name = name;
      this.maxLevel = maxLevel;
      this.treasureLevel = treasureLevel;
   }

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      PartModel.initPart(wb.getSheet(2));
      Sheet sheet = wb.getSheet(1);
      initModel(sheet);
   }

   public static void initModel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      int i;
      for(i = 2; i <= rows; ++i) {
         int rank = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int maxLevel = Tools.getCellIntValue(sheet.getCell("C" + i));
         int treasureLevel = Tools.getCellIntValue(sheet.getCell("D" + i));
         int view3D = Tools.getCellIntValue(sheet.getCell("E" + i));
         int scale = Tools.getCellIntValue(sheet.getCell("F" + i));
         if (maxLevel < 1) {
            throw new Exception("圣器 - " + sheet.getName() + " - 碎片最大等级不正确 ，第" + i);
         }

         HallowModel model = new HallowModel(rank, name, maxLevel, treasureLevel);
         modelMap.put(model.getRank(), model);
         model.setView3D(view3D);
         model.setScale(scale);
         if (rank > MaxRank) {
            MaxRank = rank;
         }

         checkPart(rank, maxLevel);
      }

      for(i = 1; i <= MaxRank; ++i) {
         if (getModel(i) == null) {
            throw new Exception("圣器模板不完整，第" + i);
         }
      }

   }

   private static void checkPart(int rank, int maxLevel) throws Exception {
      for(int i = 0; i <= maxLevel; ++i) {
         PartModel part = PartModel.getPartModel(rank, i);
         if (part == null) {
            throw new Exception("圣器 - 等阶 = " + rank + ",等级= " + i + "数据不存在");
         }
      }

   }

   public static HallowModel getModel(int rank) {
      return (HallowModel)modelMap.get(rank);
   }

   public int getScale() {
      return this.scale;
   }

   public void setScale(int scale) {
      this.scale = scale;
   }

   public int getView3D() {
      return this.view3D;
   }

   public void setView3D(int view3d) {
      this.view3D = view3d;
   }

   public int getRank() {
      return this.rank;
   }

   public void setRank(int rank) {
      this.rank = rank;
   }

   public String getName() {
      return this.name;
   }

   public int getTreasureLevel() {
      return this.treasureLevel;
   }

   public void setTreasureLevel(int treasureLevel) {
      this.treasureLevel = treasureLevel;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getMaxLevel() {
      return this.maxLevel;
   }

   public void setMaxLevel(int maxLevel) {
      this.maxLevel = maxLevel;
   }
}
