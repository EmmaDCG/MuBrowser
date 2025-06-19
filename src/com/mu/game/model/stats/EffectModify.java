package com.mu.game.model.stats;

public class EffectModify {
   private StatEnum stat;
   private int value;
   private StatModifyPriority priority;
   private boolean isMetuxed;

   public EffectModify(StatEnum stat, int value, StatModifyPriority priority, boolean isMetuxed) {
      this.stat = stat;
      this.value = value;
      this.priority = priority;
      this.isMetuxed = isMetuxed;
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

   public boolean isMetuxed() {
      return this.isMetuxed;
   }

   public void setMetuxed(boolean isMetuxed) {
      this.isMetuxed = isMetuxed;
   }
}
