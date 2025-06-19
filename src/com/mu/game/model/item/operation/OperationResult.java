package com.mu.game.model.item.operation;

public class OperationResult {
   private int result = 1;
   private long itemID;

   public OperationResult(int result, long itemID) {
      this.result = result;
      this.itemID = itemID;
   }

   public int getResult() {
      return this.result;
   }

   public long getItemID() {
      return this.itemID;
   }

   public boolean isOk() {
      return this.result == 1;
   }
}
