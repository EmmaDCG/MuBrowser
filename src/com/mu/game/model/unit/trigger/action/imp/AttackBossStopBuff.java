package com.mu.game.model.unit.trigger.action.imp;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.monster.Monster;

public class AttackBossStopBuff extends PKStopBuffAction {
   public AttackBossStopBuff(int id, int buffID) {
      super(id, buffID);
   }

   protected boolean canTrigger(Creature defencer) {
      if (defencer != null && !defencer.isDestroy()) {
         return defencer.getType() == 2 && ((Monster)defencer).isBoss();
      } else {
         return false;
      }
   }
}
