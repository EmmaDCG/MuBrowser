package com.mu.game.model.unit.trigger.action;

import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.trigger.inter.KillMonster;

public abstract class AbsKillMonsterAction extends TriggerAction implements KillMonster {
   public AbsKillMonsterAction(int id) {
      super(id);
   }

   public void handle(boolean checked, Object... objects) throws Exception {
      Monster monster = (Monster)objects[0];
      if (checked) {
         this.killMonster(monster);
      } else if (this.meedCondition()) {
         this.killMonster(monster);
      }

   }

   public int getType() {
      return 4;
   }
}
