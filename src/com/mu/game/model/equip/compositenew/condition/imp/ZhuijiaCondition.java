package com.mu.game.model.equip.compositenew.condition.imp;

import com.mu.game.model.equip.compositenew.condition.MaterialCondition;
import com.mu.game.model.item.Item;

public class ZhuijiaCondition extends MaterialCondition {
   private int zhuijiaLevel;

   public ZhuijiaCondition(int conID, int zhuijiaLevel) {
      super(conID);
      this.zhuijiaLevel = zhuijiaLevel;
   }

   public boolean suit(Item item) {
      return item.getZhuijiaLevel() >= this.zhuijiaLevel;
   }
}
