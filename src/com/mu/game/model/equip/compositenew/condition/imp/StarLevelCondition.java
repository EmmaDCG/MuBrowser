package com.mu.game.model.equip.compositenew.condition.imp;

import com.mu.game.model.equip.compositenew.condition.MaterialCondition;
import com.mu.game.model.item.Item;

public class StarLevelCondition extends MaterialCondition {
   private int starLevel;

   public StarLevelCondition(int conID, int starLevel) {
      super(conID);
      this.starLevel = starLevel;
   }

   public boolean suit(Item item) {
      return item.getStarLevel() >= this.starLevel;
   }
}
