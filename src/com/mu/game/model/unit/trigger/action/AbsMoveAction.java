package com.mu.game.model.unit.trigger.action;

import com.mu.game.model.unit.trigger.inter.Move;

public abstract class AbsMoveAction extends TriggerAction implements Move {
   public AbsMoveAction(int id) {
      super(id);
   }

   public void handle(boolean checked, Object... objects) throws Exception {
      if (checked) {
         this.move();
      } else if (this.meedCondition()) {
         this.move();
      }

   }

   public int getType() {
      return 6;
   }
}
