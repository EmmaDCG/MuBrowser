package com.mu.game.model.spiritOfWar.filter.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.spiritOfWar.filter.FilterCondition;

public class FilterExcellentCount extends FilterCondition {
   private int count;

   public FilterExcellentCount(int id, int type, String name, int count) {
      super(id, type, name);
      this.count = count;
   }

   public boolean filter(Item item) {
      int excellentCount = item.getBonusStatSize(3);
      return excellentCount <= this.count;
   }
}
