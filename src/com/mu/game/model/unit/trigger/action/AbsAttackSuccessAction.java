package com.mu.game.model.unit.trigger.action;

import com.mu.game.model.unit.trigger.inter.AttackSuccess;
import java.util.HashMap;

public abstract class AbsAttackSuccessAction extends TriggerAction implements AttackSuccess {
   public AbsAttackSuccessAction(int id) {
      super(id);
   }

   public void handle(boolean checked, Object... objects) throws Exception {
      HashMap results = (HashMap)objects[0];
      if (checked) {
         this.attackMultiple(results);
      } else if (this.meedCondition()) {
         this.attackMultiple(results);
      }

   }

   public int getType() {
      return 5;
   }
}
