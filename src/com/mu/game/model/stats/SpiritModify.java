package com.mu.game.model.stats;

public class SpiritModify extends FinalModify {
   private int increaseValue;

   public SpiritModify(StatEnum stat, int value, StatModifyPriority priority, int increaseValue) {
      super(stat, value, priority);
      this.increaseValue = increaseValue;
   }

   public int getIncreaseValue() {
      return this.increaseValue;
   }

   public void setIncreaseValue(int increaseValue) {
      this.increaseValue = increaseValue;
   }
}
