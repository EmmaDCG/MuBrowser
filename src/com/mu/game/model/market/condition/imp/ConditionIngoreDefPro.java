package com.mu.game.model.market.condition.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.market.condition.ConditionAtom;
import com.mu.game.model.stats.StatEnum;

public class ConditionIngoreDefPro extends ConditionAtom {
   public ConditionIngoreDefPro(int atomID, String name, int type) {
      super(atomID, name, type);
   }

   public boolean check(Item item) {
      if (!item.isEquipment()) {
         return false;
      } else {
         return item.getFirstStatValue(StatEnum.IGNORE_DEF_PRO) > 0;
      }
   }
}
