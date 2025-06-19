package com.mu.game.model.equip.external;

public class ExternalEntry {
   private int type;
   private int modelID;
   private int effectID;

   public ExternalEntry(int type, int modelID, int effectID) {
      this.type = type;
      this.modelID = modelID;
      this.effectID = effectID;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int externalType) {
      this.type = externalType;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getEffectID() {
      return this.effectID;
   }

   public void setEffectID(int effectID) {
      this.effectID = effectID;
   }
}
