package com.mu.game.model.item.action.imp;

import com.mu.game.model.stats.StatEnum;
import com.mu.utils.Rnd;

public class AddNumber {
   private StatEnum stat;
   private int minCount;
   private int maxCount;

   public AddNumber(StatEnum stat, int minCount, int maxCount) {
      this.stat = stat;
      this.minCount = minCount;
      this.maxCount = maxCount;
   }

   public int getRndValue() {
      return Rnd.get(this.minCount, this.maxCount);
   }

   public StatEnum getStat() {
      return this.stat;
   }

   public void setStat(StatEnum stat) {
      this.stat = stat;
   }

   public int getMinCount() {
      return this.minCount;
   }

   public void setMinCount(int minCount) {
      this.minCount = minCount;
   }

   public int getMaxCount() {
      return this.maxCount;
   }

   public void setMaxCount(int maxCount) {
      this.maxCount = maxCount;
   }
}
