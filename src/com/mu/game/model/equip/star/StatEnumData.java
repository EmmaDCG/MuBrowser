package com.mu.game.model.equip.star;

import com.mu.game.model.stats.StatEnum;

public class StatEnumData {
   private StatEnum stat;
   private int value;
   private int incremental;

   public StatEnumData(StatEnum stat, int value, int incremental) {
      this.stat = stat;
      this.value = value;
      this.incremental = incremental;
   }

   public StatEnum getStat() {
      return this.stat;
   }

   public void setStat(StatEnum stat) {
      this.stat = stat;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public int getIncremental() {
      return this.incremental;
   }

   public void setIncremental(int incremental) {
      this.incremental = incremental;
   }
}
