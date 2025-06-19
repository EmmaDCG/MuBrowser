package com.mu.game.model.item;

public class ItemStone {
   private int modelID;
   private int equipStatID;
   private int index;

   public ItemStone(int modelID, int equipStatID, int index) {
      this.modelID = modelID;
      this.index = index;
      this.equipStatID = equipStatID;
   }

   public ItemStone cloneItemStrone() {
      return new ItemStone(this.modelID, this.equipStatID, this.index);
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public int getEquipStatID() {
      return this.equipStatID;
   }

   public void setEquipStatID(int equipStatID) {
      this.equipStatID = equipStatID;
   }
}
