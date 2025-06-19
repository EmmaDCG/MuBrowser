package com.mu.game.model.stats;

public class ItemModify {
   private StatEnum stat;
   private int value;
   private StatModifyPriority priority;
   private int bonusType = 0;

   public ItemModify(StatEnum stat, int value, StatModifyPriority priority, int bonusType) {
      this.stat = stat;
      this.value = value;
      this.priority = priority;
      this.bonusType = bonusType;
   }

   public boolean isShowPercent() {
      return this.stat.isPercent() || this.priority.isPercent();
   }

   public int getShowValue() {
      return !this.stat.isPercent() && !this.priority.isPercent() ? this.value : this.value / 1000;
   }

   public String getSuffix() {
      return !this.stat.isPercent() && !this.priority.isPercent() ? "" : "%";
   }

   public ItemModify cloneModify() {
      ItemModify modify = new ItemModify(this.stat, this.value, this.priority, this.bonusType);
      return modify;
   }

   public FinalModify createFinalModify() {
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

   public int getBonusType() {
      return this.bonusType;
   }

   public void setBonusType(int bonusType) {
      this.bonusType = bonusType;
   }
}
