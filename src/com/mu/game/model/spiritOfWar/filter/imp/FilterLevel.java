package com.mu.game.model.spiritOfWar.filter.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.spiritOfWar.filter.FilterCondition;

public class FilterLevel extends FilterCondition {
   int level = -1;

   public FilterLevel(int id, int type, String name, int level) {
      super(id, type, name);
      this.level = level;
   }

   public boolean filter(Item item) {
      return item.getLevel() <= this.level;
   }
}
