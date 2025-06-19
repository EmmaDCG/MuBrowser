package com.mu.game.model.market.condition.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.market.condition.ConditionAtom;

public class ConditionNone extends ConditionAtom {
   public ConditionNone(int atomID, String name, int type) {
      super(atomID, name, type);
   }

   public boolean check(Item item) {
      return true;
   }
}
