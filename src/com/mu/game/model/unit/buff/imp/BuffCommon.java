package com.mu.game.model.unit.buff.imp;

import com.mu.game.model.stats.listener.BuffStatCalculation;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.Buff;

public class BuffCommon extends Buff {
   public BuffCommon(int modelID, int level, Creature owner, Creature caster) {
      super(modelID, level, owner, caster);
   }

   public void startDetail() {
      if (this.getProps() != null && this.getProps().size() > 0) {
         this.stats = BuffStatCalculation.addBuff(this.getOwner(), this.getProps(), this.getModelID());
      }

   }

   public void doWork(long time) {
   }

   public void endSpecial(boolean notice) {
   }
}
