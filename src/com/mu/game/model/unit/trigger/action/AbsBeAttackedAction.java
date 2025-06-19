package com.mu.game.model.unit.trigger.action;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.trigger.inter.BeAttacked;

public abstract class AbsBeAttackedAction extends TriggerAction implements BeAttacked {
   public AbsBeAttackedAction(int id) {
      super(id);
   }

   public void handle(boolean checked, Object... objects) throws Exception {
      Creature attacker = (Creature)objects[0];
      AttackResult result = (AttackResult)objects[1];
      if (checked) {
         this.beAttacked(attacker, result);
      } else if (this.meedCondition()) {
         this.beAttacked(attacker, result);
      }

   }

   public int getType() {
      return 2;
   }
}
