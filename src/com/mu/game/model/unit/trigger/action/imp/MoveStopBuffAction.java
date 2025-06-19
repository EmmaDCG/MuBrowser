package com.mu.game.model.unit.trigger.action.imp;

import com.mu.game.model.unit.trigger.action.AbsMoveAction;

public class MoveStopBuffAction extends AbsMoveAction {
   private int buffID;

   public MoveStopBuffAction(int id, int buffID) {
      super(id);
      this.buffID = buffID;
   }

   public void move() {
      this.getOwner().getBuffManager().endBuff(this.getBuffID(), true);
   }

   public boolean privyCondition() {
      return true;
   }

   public int getBuffID() {
      return this.buffID;
   }
}
