package com.mu.game.model.market.condition.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.market.condition.ConditionAtom;

public class ConditionEquipSet extends ConditionAtom {
   public ConditionEquipSet(int atomID, String name, int type) {
      super(atomID, name, type);
   }

   public boolean check(Item item) {
      if (!item.isEquipment()) {
         return false;
      } else {
         return item.isEquipSet();
      }
   }
}
