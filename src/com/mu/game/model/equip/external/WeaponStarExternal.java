package com.mu.game.model.equip.external;

import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class WeaponStarExternal {
   private static HashMap externals = new HashMap();
   private int itemModelID;
   private int starLevel;
   private int externalModelMenRight;
   private int externalModelMenLeft;

   public WeaponStarExternal(int itemModelID, int starLevel, int externalModelMenRight, int externalModelMenLeft) {
      this.itemModelID = itemModelID;
      this.starLevel = starLevel;
      this.externalModelMenRight = externalModelMenRight;
      this.externalModelMenLeft = externalModelMenLeft;
   }

   public static long getKey(int itemModelID, int starLevel) {
      return (long)starLevel * 100000000L + (long)itemModelID;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int itemModelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int starLevel = Tools.getCellIntValue(sheet.getCell("B" + i));
         int externalModelMenRight = Tools.getCellIntValue(sheet.getCell("C" + i));
         int externalModelMenLeft = Tools.getCellIntValue(sheet.getCell("D" + i));
         WeaponStarExternal external = new WeaponStarExternal(itemModelID, starLevel, externalModelMenRight, externalModelMenLeft);
         long key = getKey(itemModelID, starLevel);
         externals.put(key, external);
      }

   }

   public static WeaponStarExternal getExternal(int modelID, int starLevel) {
      long key = getKey(modelID, starLevel);
      return (WeaponStarExternal)externals.get(key);
   }

   public int getItemModelID() {
      return this.itemModelID;
   }

   public void setItemModelID(int itemModelID) {
      this.itemModelID = itemModelID;
   }

   public int getStarLevel() {
      return this.starLevel;
   }

   public void setStarLevel(int starLevel) {
      this.starLevel = starLevel;
   }

   public int getExternalModelMenRight() {
      return this.externalModelMenRight;
   }

   public void setExternalModelMenRight(int externalModelMenRight) {
      this.externalModelMenRight = externalModelMenRight;
   }

   public int getExternalModelMenLeft() {
      return this.externalModelMenLeft;
   }

   public void setExternalModelMenLeft(int externalModelMenLeft) {
      this.externalModelMenLeft = externalModelMenLeft;
   }
}
