package com.mu.game.model.equip.compositenew.condition.imp;

import com.mu.game.model.equip.compositenew.condition.MaterialCondition;
import com.mu.game.model.item.Item;

public class ExcellentEquipCondition extends MaterialCondition {
   private int excellentCount = 1;

   public ExcellentEquipCondition(int conID, int excellentCount) {
      super(conID);
   }

   public boolean suit(Item item) {
      int ec = item.getBonusStatSize(3);
      return ec >= this.excellentCount;
   }
}
