package com.mu.game.model.market.condition.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.market.condition.ConditionAtom;

public class ConditionExcellentCount extends ConditionAtom {
   int excellentCount = 0;

   public ConditionExcellentCount(int atomID, String name, int type, int count) {
      super(atomID, name, type);
      this.excellentCount = count;
   }

   public boolean check(Item item) {
      if (!item.isEquipment()) {
         return false;
      } else {
         return item.getBonusStatSize(3) >= this.excellentCount;
      }
   }
}
