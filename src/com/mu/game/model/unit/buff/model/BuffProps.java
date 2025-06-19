package com.mu.game.model.unit.buff.model;

import com.mu.config.Constant;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.game.model.unit.Creature;

public class BuffProps {
   private StatEnum stat;
   private int value;
   private int maxValue;
   private StatModifyPriority priority;
   private int strCoeff;
   private int effectType = 1;

   public BuffProps(StatEnum stat, int value, int maxValue, StatModifyPriority priority, int strCoeff) {
      this.stat = stat;
      this.value = value;
      this.maxValue = maxValue;
      this.priority = priority;
      this.strCoeff = strCoeff;
   }

   public BuffProps cloneProp() {
      return new BuffProps(this.stat, this.value, this.maxValue, this.priority, this.strCoeff);
   }

   public BuffProps cloneProp(Creature castor, StatEnum strStat) {
      int newValue = this.value;
      if (strStat != null && castor != null) {
         newValue += Constant.getPercentValue(castor.getStatValue(strStat), this.strCoeff);
         newValue = Math.min(newValue, this.maxValue);
      }

      return new BuffProps(this.stat, newValue, this.maxValue, this.priority, this.strCoeff);
   }

   public boolean isPercent() {
      if (this.stat.isPercent()) {
         return true;
      } else {
         return this.priority != StatModifyPriority.ADD && this.priority != StatModifyPriority.NONE;
      }
   }

   public void addValue(int addValue) {
      this.value += addValue;
      this.maxValue += addValue;
   }

   public int getStrCoeff() {
      return this.strCoeff;
   }

   public void setStrCoeff(int strCoeff) {
      this.strCoeff = strCoeff;
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

   public int getEffectType() {
      return this.effectType;
   }

   public void setEffectType(int effectType) {
      this.effectType = effectType;
   }

   public int getMaxValue() {
      return this.maxValue;
   }

   public void setMaxValue(int maxValue) {
      this.maxValue = maxValue;
   }
}
