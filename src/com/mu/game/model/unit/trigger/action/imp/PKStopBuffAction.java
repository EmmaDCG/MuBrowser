package com.mu.game.model.unit.trigger.action.imp;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.trigger.action.AbsAttackAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class PKStopBuffAction extends AbsAttackAction {
   private int buffID;

   public PKStopBuffAction(int id, int buffID) {
      super(id);
      this.buffID = buffID;
   }

   public void attackMultiple(HashMap results) {
      boolean flag = false;
      Iterator it = results.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         flag = this.canTrigger((Creature)entry.getKey());
         if (flag) {
            break;
         }
      }

      if (flag) {
         this.getOwner().getBuffManager().endBuff(this.getBuffID(), true);
      }

   }

   protected boolean canTrigger(Creature defencer) {
      if (defencer != null && !defencer.isDestroy()) {
         return defencer.getType() == 1;
      } else {
         return false;
      }
   }

   public boolean privyCondition() {
      return true;
   }

   public int getBuffID() {
      return this.buffID;
   }
}
