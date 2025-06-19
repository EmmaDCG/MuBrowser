package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.operation.OperationResult;
import java.util.ArrayList;
import java.util.List;

public class AddItemByObjOperation extends AbsAddOrDelItemOperation {
   private Item item = null;
   private int source;

   public AddItemByObjOperation(Item item, int source) {
      this.item = item;
      this.source = source;
   }

   public OperationResult execute() throws Exception {
      Storage storage = this.getTargetStorage(this.item.getItemSort(), this.source);
      if (specialActionWhenAdd(this.getPlayer(), this.item.getModel(), this.item.getCount(), this.item.isBind(), this.source)) {
         return new OperationResult(this.result, this.itemID);
      } else {
         if (this.item.getModel().getMaxStackCount() != 1 && !this.isTransaction(this.source)) {
            ItemDataUnit unit = new ItemDataUnit(this.item.getModelID(), this.item.getCount(), this.item.isBind());
            unit.setExpireTime(this.item.getExpireTime());
            unit.setSource(this.source);
            List unitList = new ArrayList();
            unitList.add(unit);
            int canPut = this.canPutToContainer(unitList);
            if (canPut == 1) {
               this.itemID = this.addItemDetail(unit, storage);
            } else {
               this.result = canPut;
            }

            unitList.clear();
         } else {
            int slot = storage.getNextSlot();
            if (slot == -1) {
               this.result = 2004;
               return new OperationResult(this.result, this.itemID);
            }

            Item cloneItem = this.getRealItem(this.item, this.source);
            this.doAddToContainerAction(cloneItem, slot, storage, this.source);
            this.itemID = cloneItem.getID();
         }

         return new OperationResult(this.result, this.itemID);
      }
   }

   private Item getRealItem(Item item, int source) {
      return item.cloneItem(1);
   }
}
