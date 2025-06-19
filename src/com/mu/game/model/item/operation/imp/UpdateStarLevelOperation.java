package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemRune;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;
import java.util.Iterator;
import java.util.List;

public class UpdateStarLevelOperation extends ItemOperation {
   protected Item item = null;
   private int starLevel;
   private List runes = null;

   public UpdateStarLevelOperation(Item item, int starLevel, List runes) {
      this.item = item;
      this.starLevel = starLevel;
      this.runes = runes;
   }

   public OperationResult execute() throws Exception {
      if (this.item != null && this.item.getCount() >= 1) {
         this.doStarUpdate();
         if (this.runes != null && this.runes.size() > 0) {
            this.item.getRunes().clear();
            Iterator var2 = this.runes.iterator();

            while(var2.hasNext()) {
               ItemRune rune = (ItemRune)var2.next();
               this.item.addRune(rune.cloneRune(), false);
            }
         }

         this.updateItemOtherOperation(this.item, 21, 5);
         return new OperationResult(1, this.item.getID());
      } else {
         return new OperationResult(3002, -1L);
      }
   }

   protected void doStarUpdate() {
      this.item.setStarUpTimes(0);
      if (this.item.getOnceMaxStarLevel() < this.starLevel) {
         this.item.setOnceMaxStarLevel(this.starLevel);
      }

      this.item.changeStarLevel(this.starLevel);
   }
}
