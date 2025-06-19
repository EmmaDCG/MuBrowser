package com.mu.game.model.unit.player.pkMode;

import com.mu.utils.Tools;
import jxl.Sheet;

public enum EvilEnum {
   Evil_Orange(1, 5, "", 1),
   Evil_White(2, 5, "", 1),
   Evil_Gray(3, 2, "", 1),
   Evil_Red(4, 1, "", 1);

   private int evilId;
   private int level;
   private String color;
   private int font;

   private EvilEnum(int evilId, int level, String color, int font) {
      this.evilId = evilId;
      this.level = level;
      this.color = color;
      this.font = font;
   }

   public static EvilEnum find(int evilID) {
      EvilEnum[] var4;
      int var3 = (var4 = values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         EvilEnum eEnum = var4[var2];
         if (eEnum.getEvilId() == evilID) {
            return eEnum;
         }
      }

      return Evil_White;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int evilID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String color = Tools.getCellValue(sheet.getCell("C" + i));
         int font = Tools.getCellIntValue(sheet.getCell("D" + i));
         EvilEnum ee = find(evilID);
         ee.setColor(color);
         ee.setFont(font);
      }

   }

   public int getEvilId() {
      return this.evilId;
   }

   public int getLevel() {
      return this.level;
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
