package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.container.Container;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;

public class UpdateItemContainerOperation extends ItemOperation {
   private Item item;
   private int ts;
   private int slot;

   public UpdateItemContainerOperation(Item item, int ts, int slot) {
      this.item = item;
      this.ts = ts;
      this.slot = slot;
   }

   public OperationResult execute() throws Exception {
      if (this.item == null) {
         return new OperationResult(3002, -1L);
      } else {
         Container tc = this.getContainer(this.ts);
         Container fc = this.getContainer(this.item.getContainerType());
         if (tc != null && fc != null) {
            if (this.slot != -1) {
               if (tc.getItemBySlot(this.slot) != null) {
                  this.slot = tc.getNextSlot();
               }
            } else {
               this.slot = tc.getNextSlot();
            }

            if (this.slot == -1) {
               return new OperationResult(2004, -1L);
            } else {
               int fs = this.item.getContainerType();
               fc.moveAwayfromContainer(this.item);
               tc.putToContainerBySlot(this.item, this.slot);
               this.doUpdateItemAction(this.item, tc, 4, 0, 17, fs);
               return new OperationResult(1, this.item.getID());
            }
         } else {
            return new OperationResult(2005, -1L);
         }
      }
   }
}
