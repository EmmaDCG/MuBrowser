package com.mu.game.model.spiritOfWar.filter.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.spiritOfWar.filter.FilterCondition;

public class FilterEquipSet extends FilterCondition {
   private int value = 0;

   public FilterEquipSet(int id, int type, String name, int value) {
      super(id, type, name);
      this.value = value;
   }

   public boolean filter(Item item) {
      return this.value != 0 || !item.isEquipSet();
   }
}
