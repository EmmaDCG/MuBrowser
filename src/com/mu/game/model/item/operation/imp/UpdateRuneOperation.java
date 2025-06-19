package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemRune;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;

public class UpdateRuneOperation extends ItemOperation {
   private Item item;
   private int id;
   private boolean mosaic;

   public UpdateRuneOperation(Item item, int id, boolean mosaic) {
      this.item = item;
      this.id = id;
      this.mosaic = mosaic;
   }

   public OperationResult execute() throws Exception {
      if (this.item != null && this.item.getCount() >= 1) {
         if (this.mosaic) {
            this.item.addRune(new ItemRune(this.id, this.item.getRunes().size()), true);
         } else {
            this.item.removeRuneByIndex(this.id);
         }

         this.updateItemOtherOperation(this.item, 21, 10);
         return new OperationResult(1, this.item.getID());
      } else {
         return new OperationResult(3002, -1L);
      }
   }
}
