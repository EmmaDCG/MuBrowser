package com.mu.game.model.equip.durability;

public class DurabilityData {
   public static final int TType_StatOnly = 1;
   public static final int TType_UnEffect = 2;
   private int itemType;
   private float parameter;
   private int effectType;

   public DurabilityData(int itemType, float parameter, int effectType) {
      this.itemType = itemType;
      this.parameter = parameter;
      this.effectType = effectType;
   }

   public boolean isStatOnly() {
      return this.effectType == 1;
   }

   public int getItemType() {
      return this.itemType;
   }

   public void setItemType(int itemType) {
      this.itemType = itemType;
   }

   public float getParameter() {
      return this.parameter;
   }

   public void setParameter(float parameter) {
      this.parameter = parameter;
   }

   public int getEffectType() {
      return this.effectType;
   }

   public void setEffectType(int effectType) {
      this.effectType = effectType;
   }
}
