package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;

public class MoveToOtherContainerOperation extends ItemOperation {
   private int fromS;
   private int toS;
   private int slot;
   private long itemID = -1L;
   private int result = 1;

   public MoveToOtherContainerOperation(int fromS, int toS, long itemID, int slot) {
      this.fromS = fromS;
      this.toS = toS;
      this.itemID = itemID;
      this.slot = slot;
   }

   public OperationResult execute() throws Exception {
      Storage fs = this.getPlayer().getStorage(this.fromS);
      Storage ts = this.getPlayer().getStorage(this.toS);
      if (fs != null && ts != null) {
         Item item = fs.getItemByID(this.itemID);
         this.result = this.canMove(fs, ts, item);
         if (this.result != 1) {
            return new OperationResult(this.result, this.itemID);
         } else {
            Item tempItem = null;
            boolean flag = false;
            if (this.slot == -1) {
               this.slot = ts.getNextSlot();
            } else {
               tempItem = ts.getItemBySlot(this.slot);
               if (tempItem != null) {
                  if (tempItem.getCount() < tempItem.getModel().getMaxStackCount() && ItemTools.canStack(tempItem, item)) {
                     flag = true;
                  } else {
                     this.slot = ts.getNextSlot();
                  }
               }
            }

            if (flag) {
               int needCount = 0;
               if (tempItem.getCount() + item.getCount() <= tempItem.getModel().getMaxStackCount()) {
                  needCount = item.getCount();
                  tempItem.increaseCount(item.getCount(), item.getExpireTime());
                  this.doRemoveItemAction(item, needCount, fs, 17);
                  this.doUpdateItemAction(tempItem, ts, 1, needCount, 17, this.toS);
                  this.result = 2009;
               } else {
                  needCount = tempItem.getModel().getMaxStackCount() - tempItem.getCount();
                  tempItem.increaseCount(needCount, item.getExpireTime());
                  item.decreaseCount(needCount);
                  this.result = 2009;
                  this.slot = ts.getNextSlot();
                  if (this.slot != -1) {
                     fs.moveAwayfromContainer(item);
                     ts.putToContainerBySlot(item, this.slot);
                     this.result = 1;
                  }

                  this.doUpdateItemAction(tempItem, ts, 1, needCount, 17, this.toS);
                  if (this.slot == -1) {
                     this.doUpdateItemAction(item, fs, 1, -needCount, 17, this.fromS);
                  } else {
                     this.doUpdateItemAction(item, ts, 4, -needCount, 17, this.fromS);
                  }
               }
            } else if (this.slot == -1) {
               if (this.fromS == 4) {
                  this.result = 2006;
               } else {
                  this.result = 2004;
               }
            } else {
               fs.moveAwayfromContainer(item);
               ts.putToContainerBySlot(item, this.slot);
               this.doUpdateItemAction(item, ts, 4, 0, 17, this.fromS);
               this.result = 1;
            }

            return new OperationResult(this.result, this.itemID);
         }
      } else {
         return new OperationResult(2005, this.itemID);
      }
   }

   private int canMove(Storage fromStorage, Storage toStorage, Item item) {
      if (item != null && item.getCount() > 0) {
         if (this.toS == 14) {
            return 2007;
         } else if (this.fromS == this.toS) {
            return 2008;
         } else {
            return fromStorage.getType() == 1 && !item.getModel().isCanPutToWareHouse() ? 3013 : 1;
         }
      } else {
         return 3002;
      }
   }
}
