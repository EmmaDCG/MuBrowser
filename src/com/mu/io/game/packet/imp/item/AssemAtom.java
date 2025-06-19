package com.mu.io.game.packet.imp.item;

import com.mu.game.model.stats.ItemModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;

public class AssemAtom extends ItemModify {
   private int maxValue;
   private int incremental;

   public AssemAtom(StatEnum stat, int value, StatModifyPriority priority, int bonusType) {
      super(stat, value, priority, bonusType);
   }

   public static AssemAtom createAtom(ItemModify modify) {
      AssemAtom atom = new AssemAtom(modify.getStat(), modify.getValue(), modify.getPriority(), modify.getBonusType());
      return atom;
   }

   public int getMaxValue() {
      return this.maxValue;
   }

   public void setMaxValue(int maxValue) {
      this.maxValue = maxValue;
   }

   public int getIncremental() {
      return this.incremental;
   }

   public void setIncremental(int incremental) {
      this.incremental = incremental;
   }
}
