package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;

public class UpgradeItemOperation extends ItemOperation {
   private Item item = null;
   private int newModelId = -1;

   public UpgradeItemOperation(Item item, int newModelID) {
      this.item = item;
      this.newModelId = newModelID;
   }

   public OperationResult execute() throws Exception {
      if (this.item != null && this.item.getCount() >= 1) {
         if (ItemModel.getModel(this.newModelId) == null) {
            return new OperationResult(3002, this.item.getID());
         } else {
            this.item.reset(this.newModelId);
            this.updateItemOtherOperation(this.item, 21, 15);
            return new OperationResult(1, this.item.getID());
         }
      } else {
         return new OperationResult(3002, -1L);
      }
   }
}
