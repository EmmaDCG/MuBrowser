package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;

public class UpdateZhuijiaLevelOperation extends ItemOperation {
   private Item item = null;
   private int newLevel = 0;

   public UpdateZhuijiaLevelOperation(Item item, int newLevel) {
      this.item = item;
      this.newLevel = newLevel;
   }

   public OperationResult execute() throws Exception {
      if (this.item != null && this.item.getCount() >= 1) {
         int oldLevel = this.item.getZhuijiaLevel();
         if (oldLevel != this.newLevel) {
            this.item.changeZhuijiaLevel(this.newLevel);
            this.updateItemOtherOperation(this.item, 21, 14);
         }

         return new OperationResult(1, this.item.getID());
      } else {
         return new OperationResult(3002, -1L);
      }
   }
}
