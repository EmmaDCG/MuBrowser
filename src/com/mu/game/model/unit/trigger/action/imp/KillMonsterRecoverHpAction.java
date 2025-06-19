package com.mu.game.model.unit.trigger.action.imp;

import com.mu.config.Constant;
import com.mu.game.model.item.action.imp.AddHp;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.trigger.action.AbsKillMonsterAction;

public class KillMonsterRecoverHpAction extends AbsKillMonsterAction {
   private int value;

   public KillMonsterRecoverHpAction(int id, int value) {
      super(id);
      this.value = value;
   }

   public void killMonster(Monster monster) {
      int tmpValue = Constant.getPercentValue(this.getOwner().getMaxHp(), this.value);
      AddHp.addHPAndSend(this.getOwner(), Math.min(tmpValue, this.getOwner().getMaxHp() - this.getOwner().getHp()));
   }

   public boolean privyCondition() {
      return this.getOwner().getHp() < this.getOwner().getMaxHp();
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }
}
