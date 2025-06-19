package com.mu.game.model.spiritOfWar.filter.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.spiritOfWar.filter.FilterCondition;

public class FilterStarLevel extends FilterCondition {
   private int value = -1;

   public FilterStarLevel(int id, int type, String name, int value) {
      super(id, type, name);
      this.value = value;
   }

   public boolean filter(Item item) {
      return item.getStarLevel() <= this.value;
   }
}
