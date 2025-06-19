package com.mu.game.model.equip.compositenew.condition.imp;

import com.mu.game.model.equip.compositenew.condition.MaterialCondition;
import com.mu.game.model.item.Item;

public class EquipSetCondition extends MaterialCondition {
   public EquipSetCondition(int conID) {
      super(conID);
   }

   public boolean suit(Item item) {
      if (item.getModel().getSort() != 1) {
         return false;
      } else {
         return item.getModel().getSets() != -1;
      }
   }
}
