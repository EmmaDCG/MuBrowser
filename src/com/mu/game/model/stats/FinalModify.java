package com.mu.game.model.stats;

public class FinalModify {
   private StatEnum stat;
   private int value;
   private StatModifyPriority priority;

   public FinalModify(StatEnum stat, int value, StatModifyPriority priority) {
      this.stat = stat;
      this.value = value;
      this.priority = priority;
   }

   public boolean isShortLived() {
      return false;
   }

   public boolean isPercent() {
      return this.priority.isPercent();
   }

   public boolean isShowPercent() {
      return this.priority.isPercent() || this.stat.isPercent();
   }

   public int getShowValue() {
      return !this.isPercent() && !this.stat.isPercent() ? this.value : this.value / 1000;
   }

   public String getSuffix() {
      return !this.isPercent() && !this.stat.isPercent() ? "" : "%";
   }

   public FinalModify cloneModify() {
      return new FinalModify(this.stat, this.value, this.priority);
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

   public StatModifyPriority getPriority() {
      return this.priority;
   }

   public void setPriority(StatModifyPriority priority) {
      this.priority = priority;
   }
}
