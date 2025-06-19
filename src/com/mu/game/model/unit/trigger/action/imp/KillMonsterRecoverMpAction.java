package com.mu.game.model.unit.trigger.action.imp;

import com.mu.config.Constant;
import com.mu.game.model.item.action.imp.AddMp;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.trigger.action.AbsKillMonsterAction;

public class KillMonsterRecoverMpAction extends AbsKillMonsterAction {
   private int value;

   public KillMonsterRecoverMpAction(int id, int value) {
      super(id);
      this.value = value;
   }

   public void killMonster(Monster monster) {
      int tmpValue = Constant.getPercentValue(this.getOwner().getMaxMp(), this.value);
      AddMp.addMPAndSend(this.getOwner(), Math.min(tmpValue, this.getOwner().getMaxMp() - this.getOwner().getMp()));
   }

   public boolean privyCondition() {
      return this.getOwner().getMp() < this.getOwner().getMaxMp();
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }
}
