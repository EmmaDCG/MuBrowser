package com.mu.game.model.unit.skill.learnCondition;

import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;

public class ItemCondition implements LearnCondition {
   private int itemID;
   private int count;

   public ItemCondition(int itemID, int count) {
      this.itemID = itemID;
      this.count = count;
   }

   public int verify(Player player) {
      if (!player.getBackpack().hasEnoughItem(this.itemID, this.count)) {
         return 3001;
      } else {
         ItemModel model = ItemModel.getModel(this.itemID);
         int result = ItemAction.canUse(model, player);
         return result;
      }
   }

   public int getType() {
      return 2;
   }
}
