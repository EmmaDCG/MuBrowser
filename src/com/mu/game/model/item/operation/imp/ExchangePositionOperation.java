package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.io.game.packet.imp.storage.SortoutStorage;
import java.util.ArrayList;

public class ExchangePositionOperation extends ItemOperation {
   private int containerType;
   private int firstPosition;
   private int secondPosition;

   public ExchangePositionOperation(int containerType, int firstPosition, int secondPosition) {
      this.containerType = containerType;
      this.firstPosition = firstPosition;
      this.secondPosition = secondPosition;
   }

   public OperationResult execute() throws Exception {
      if (this.firstPosition == this.secondPosition) {
         return new OperationResult(2008, -1L);
      } else {
         Storage storage = this.getPlayer().getStorage(this.containerType);
         if (storage == null) {
            return new OperationResult(2005, -1L);
         } else if (storage.effectiveSlot(this.firstPosition) && storage.effectiveSlot(this.secondPosition)) {
            Item item = storage.getItemBySlot(this.firstPosition);
            if (item != null && item.getCount() > 0) {
               Item sItem = storage.getItemBySlot(this.secondPosition);
               if (sItem != null && ItemTools.canStack(item, sItem) && item.getCount() + sItem.getCount() <= item.getModel().getMaxStackCount()) {
                  sItem.increaseCount(item.getCount(), item.getExpireTime());
                  this.doUpdateItemAction(sItem, storage, 1, item.getCount(), 15, storage.getType());
                  this.doRemoveItemAction(item, item.getCount(), storage, 15);
               } else {
                  storage.exchangePosition(this.firstPosition, this.secondPosition);
                  ArrayList items = new ArrayList();
                  items.add(item);
                  this.doUpdateItemAction(item, storage, 2, item.getCount(), 16, storage.getType());
                  if (sItem != null) {
                     this.doUpdateItemAction(sItem, storage, 2, item.getCount(), 16, storage.getType());
                     items.add(sItem);
                  }

                  SortoutStorage.itemMoveResult(this.containerType, items, this.getPlayer());
               }

               return new OperationResult(1, -1L);
            } else {
               return new OperationResult(3002, -1L);
            }
         } else {
            return new OperationResult(3009, -1L);
         }
      }
   }
}
