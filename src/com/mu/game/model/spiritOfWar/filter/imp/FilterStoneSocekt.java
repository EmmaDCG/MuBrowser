package com.mu.game.model.spiritOfWar.filter.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.spiritOfWar.filter.FilterCondition;

public class FilterStoneSocekt extends FilterCondition {
   private int value;

   public FilterStoneSocekt(int id, int type, String name, int value) {
      super(id, type, name);
      this.value = value;
   }

   public boolean filter(Item item) {
      int itemSocket = item.getSocket();
      return itemSocket <= this.value;
   }
}
