package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemRune;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;
import java.util.List;

public class RuneInheritOperation extends ItemOperation {
   private Item targetItem;
   private Item materialItem;

   public RuneInheritOperation(Item targetItem, Item materialItem) {
      this.targetItem = targetItem;
      this.materialItem = materialItem;
   }

   public OperationResult execute() throws Exception {
      if (this.targetItem != null && this.targetItem.getCount() >= 1) {
         if (this.materialItem != null && this.materialItem.getCount() >= 1) {
            List runes = this.materialItem.getRunes();
            this.targetItem.getRunes().clear();

            for(int i = 0; i < runes.size(); ++i) {
               ItemRune rune = (ItemRune)runes.get(i);
               this.targetItem.addRune(new ItemRune(rune.getModelID(), i), true);
            }

            runes.clear();
            this.updateItemOtherOperation(this.targetItem, 21, 10);
            this.updateItemOtherOperation(this.materialItem, 21, 10);
            return new OperationResult(1, this.targetItem.getID());
         } else {
            return new OperationResult(3002, -1L);
         }
      } else {
         return new OperationResult(3002, -1L);
      }
   }
}
