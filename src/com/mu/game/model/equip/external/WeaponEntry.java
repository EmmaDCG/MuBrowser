package com.mu.game.model.equip.external;

import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class WeaponEntry {
   private static HashMap entries = new HashMap();
   private int ID;
   private int moveType;
   private int modelID;
   private int scale = 100;
   private int trajectoryEffectID = -1;
   private int knifeShadowEffect = -1;

   public WeaponEntry(int ID, int moveType, int modelID, int scale) {
      this.ID = ID;
      this.moveType = moveType;
      this.modelID = modelID;
      this.scale = scale;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int ID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int moveType = Tools.getCellIntValue(sheet.getCell("B" + i));
         int modelID = Tools.getCellIntValue(sheet.getCell("C" + i));
         int scale = (int)(Float.parseFloat(Tools.getCellValue(sheet.getCell("D" + i))) * 100.0F);
         int trajectoryEffectID = Tools.getCellIntValue(sheet.getCell("E" + i));
         int knifeShadowEffect = Tools.getCellIntValue(sheet.getCell("F" + i));
         WeaponEntry entry = new WeaponEntry(ID, moveType, modelID, scale);
         entry.setTrajectoryEffectID(trajectoryEffectID);
         entry.setKnifeShadowEffect(knifeShadowEffect);
         addWeaponEntry(entry);
      }

   }

   public static void addWeaponEntry(WeaponEntry entry) {
      entries.put(entry.getID(), entry);
   }

   public static HashMap getEntries() {
      return entries;
   }

   public static WeaponEntry getEntry(int ID) {
      return (WeaponEntry)entries.get(ID);
   }

   public int getID() {
      return this.ID;
   }

   public void setID(int iD) {
      this.ID = iD;
   }

   public int getMoveType() {
      return this.moveType;
   }

   public void setMoveType(int moveType) {
      this.moveType = moveType;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getScale() {
      return this.scale;
   }

   public void setScale(int scale) {
      this.scale = scale;
   }

   public int getTrajectoryEffectID() {
      return this.trajectoryEffectID;
   }

   public void setTrajectoryEffectID(int trajectoryEffectID) {
      this.trajectoryEffectID = trajectoryEffectID;
   }

   public int getKnifeShadowEffect() {
      return this.knifeShadowEffect;
   }

   public void setKnifeShadowEffect(int knifeShadowEffect) {
      this.knifeShadowEffect = knifeShadowEffect;
   }
}
