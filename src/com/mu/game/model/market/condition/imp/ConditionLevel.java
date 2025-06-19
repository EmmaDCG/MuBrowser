package com.mu.game.model.market.condition.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.market.condition.ConditionAtom;

public class ConditionLevel extends ConditionAtom {
   private int level;

   public ConditionLevel(int atomID, String name, int type, int level) {
      super(atomID, name, type);
      this.level = level;
   }

   public boolean check(Item item) {
      if (this.level == -1) {
         return true;
      } else {
         return item.getLevel() == this.level;
      }
   }
}
