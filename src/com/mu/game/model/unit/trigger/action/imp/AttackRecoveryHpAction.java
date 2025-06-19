package com.mu.game.model.unit.trigger.action.imp;

import com.mu.config.Constant;
import com.mu.game.model.item.action.imp.AddHp;
import com.mu.game.model.unit.trigger.action.AbsAttackAction;
import java.util.HashMap;

public class AttackRecoveryHpAction extends AbsAttackAction {
   private int value;

   public AttackRecoveryHpAction(int id, int value) {
      super(id);
      this.value = value;
   }

   public void attackMultiple(HashMap results) {
      if (results != null && results.size() >= 1) {
         int tmpValue = Constant.getPercentValue(this.getOwner().getMaxHp(), this.value);
         tmpValue = Math.max(0, Math.min(tmpValue, this.getOwner().getMaxHp() - tmpValue));
         if (tmpValue > 0) {
            AddHp.addHPAndSend(this.getOwner(), tmpValue);
         }

      }
   }

   public boolean privyCondition() {
      return !this.getOwner().isDie();
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }
}
