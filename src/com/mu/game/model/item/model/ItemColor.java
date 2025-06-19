package com.mu.game.model.item.model;

import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.List;
import jxl.Sheet;

public enum ItemColor {
   COLOR_WHITE(1, "普通", 1, 3, "", 1),
   COLOR_BLUE(2, "精良", 2, 3, "", 2),
   COLOR_GREEN(3, "珍品", 3, 3, "", 2),
   COLOR_ORANGE(4, "稀世", 4, 3, "", 2),
   COLOR_RED(5, "无双", 5, 4, "", 2),
   COLOR_YELLOW(6, "黄色", 6, 5, "", 2);

   private static List list = new ArrayList();
   private final int identity;
   private String name;
   private int extra;
   private int stoneCount;
   private String color;
   private int font;
   private String newColor;

   private ItemColor(int identity, String name, int extra, int stoneCount, String color, int font) {
      this.identity = identity;
      this.name = name;
      this.extra = extra;
      this.stoneCount = stoneCount;
      this.color = color;
      this.font = font;
   }

   public String getNewColor() {
      return this.newColor;
   }

   public void setNewColor(String newColor) {
      this.newColor = newColor;
   }

   public static ItemColor find(int identity) {
      switch(identity) {
      case 1:
         return COLOR_WHITE;
      case 2:
         return COLOR_BLUE;
      case 3:
         return COLOR_GREEN;
      case 4:
         return COLOR_ORANGE;
      case 5:
         return COLOR_RED;
      case 6:
         return COLOR_YELLOW;
      default:
         return COLOR_WHITE;
      }
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      int id;
      for(int i = 2; i <= rows; ++i) {
         id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         String c = Tools.getCellValue(sheet.getCell("C" + i));
         int font = Tools.getCellIntValue(sheet.getCell("D" + i));
         String nc = Tools.getCellValue(sheet.getCell("F" + i));
         ItemColor ic = find(id);
         ic.setColor(c);
         ic.setName(name);
         ic.setFont(font);
         ic.setNewColor(nc);
      }

      ItemColor[] var11;
      int var10 = (var11 = values()).length;

      for(id = 0; id < var10; ++id) {
         ItemColor color = var11[id];
         list.add(color);
      }

   }

   public static List getAllColors() {
      return list;
   }

   public int getExtra() {
      return this.extra;
   }

   public void setExtra(int extra) {
      this.extra = extra;
   }

   public String getName() {
      return this.name;
   }

   public int getIdentity() {
      return this.identity;
   }

   public int getStoneCount() {
      return this.stoneCount;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setStoneCount(int stoneCount) {
      this.stoneCount = stoneCount;
   }

   public String getColor() {
      return this.color;
   }

   public void setColor(String color) {
      this.color = color;
   }

   public int getFont() {
      return this.font;
   }

   public void setFont(int font) {
      this.font = font;
   }
}
