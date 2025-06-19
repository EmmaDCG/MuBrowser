package com.mu.game.model.unit.trigger.action;

import com.mu.game.model.unit.trigger.inter.AttackMultiple;
import java.util.HashMap;

public abstract class AbsAttackAction extends TriggerAction implements AttackMultiple {
   public AbsAttackAction(int id) {
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
      return 1;
   }
}
