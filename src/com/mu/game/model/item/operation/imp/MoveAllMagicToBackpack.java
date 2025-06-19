package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;
import java.util.Iterator;
import java.util.List;

public class MoveAllMagicToBackpack extends ItemOperation {
   int result = 1;

   public OperationResult execute() throws Exception {
      Storage backpack = this.getPlayer().getBackpack();
      Storage treasure = this.getPlayer().getTreasureHouse();
      this.result = this.canMove(backpack, treasure);
      if (this.result == 1) {
         List allItems = treasure.getAllItems();
         Iterator var6 = allItems.iterator();

         while(var6.hasNext()) {
            Item item = (Item)var6.next();
            int slot = backpack.getNextSlot();
            if (slot == -1) {
               break;
            }

            treasure.moveAwayfromContainer(item);
            backpack.putToContainerBySlot(item, slot);
            this.doUpdateItemAction(item, backpack, 4, 0, 17, treasure.getType());
         }
      }

      return new OperationResult(this.result, -1L);
   }

   private int canMove(Storage backpack, Storage treasure) {
      int size = treasure.getCurrentCount();
      if (size < 1) {
         return 3002;
      } else {
         return backpack.getVacantSize() < 1 ? 2004 : 1;
      }
   }
}
