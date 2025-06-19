package com.mu.game.model.item.operation.imp;

public class TempItem {
   private long itemID;
   private int count;
   private int slot;

   public TempItem(long itemID, int count, int slot) {
      this.itemID = itemID;
      this.count = count;
      this.slot = slot;
   }

   public long getItemID() {
      return this.itemID;
   }

   public void setItemID(long itemID) {
      this.itemID = itemID;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public int getSlot() {
      return this.slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }
}
