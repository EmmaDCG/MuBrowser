package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;

public class SplitItemOperation extends ItemOperation {
   private int count;
   private int containerType;
   private int slot;
   private long itemID;
   private int result = 1;

   public SplitItemOperation(long itemID, int count, int containerType) {
      this.count = count;
      this.containerType = containerType;
      this.itemID = itemID;
   }

   public OperationResult execute() throws Exception {
      Storage storage = this.getPlayer().getStorage(this.containerType);
      Item item = storage.getItemByID(this.itemID);
      this.result = this.canSplit(item, this.count, storage);
      if (this.result != 1) {
         return new OperationResult(this.result, this.itemID);
      } else {
         Item newItem = item.cloneItem(1);
         newItem.setCount(this.count);
         item.decreaseCount(this.count);
         this.doUpdateItemAction(item, storage, 1, -this.count, 12, storage.getType());
         this.doAddToContainerAction(newItem, this.slot, storage, 12);
         return new OperationResult(this.result, this.itemID);
      }
   }

   private int canSplit(Item item, int count, Storage storage) {
      if (item == null) {
         return 3002;
      } else if (storage.isFull()) {
         return this.containerType == 1 ? 2004 : 2006;
      } else if (item.getCount() <= count) {
         return 3011;
      } else if (item.getModel().getMaxStackCount() != 1 && item.getModel().isCanSplit()) {
         this.slot = storage.getNextSlot();
         if (this.slot == -1) {
            return this.containerType == 4 ? 2006 : 2004;
         } else {
            return 1;
         }
      } else {
         return 3012;
      }
   }
}
