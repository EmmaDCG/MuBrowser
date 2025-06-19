package com.mu.game.model.equip.external;

import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class EquipmentEffect {
   private static HashMap effectes = new HashMap();
   private int modelID;
   private int starLevel;
   private int effectID;

   public EquipmentEffect(int modelID, int starLevel, int effectID) {
      this.modelID = modelID;
      this.starLevel = starLevel;
      this.effectID = effectID;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int modelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int starLevel = Tools.getCellIntValue(sheet.getCell("B" + i));
         int effectID = Tools.getCellIntValue(sheet.getCell("C" + i));
         EquipmentEffect effect = new EquipmentEffect(modelID, starLevel, effectID);
         int key = createKey(modelID, starLevel);
         effectes.put(key, effect);
      }

   }

   public static int createKey(int modelID, int starLevel) {
      return (starLevel + 1) * 10000000 + modelID;
   }

   public static int getExternalEffectID(int modelID, int starLevel) {
      EquipmentEffect effect = (EquipmentEffect)effectes.get(createKey(modelID, starLevel));
      return effect != null ? effect.getEffectID() : 0;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getStarLevel() {
      return this.starLevel;
   }

   public void setStarLevel(int starLevel) {
      this.starLevel = starLevel;
   }

   public void setEffectID(int effectID) {
      this.effectID = effectID;
   }

   public int getEffectID() {
      return this.effectID;
   }
}
