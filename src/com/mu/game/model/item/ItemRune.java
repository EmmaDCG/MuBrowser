package com.mu.game.model.item;

public class ItemRune {
   private int modelID;
   private int index;

   public ItemRune(int modelID, int index) {
      this.modelID = modelID;
      this.index = index;
   }

   public ItemRune cloneRune() {
      return new ItemRune(this.modelID, this.index);
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
}
