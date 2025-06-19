package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;

public class VIPEvent extends Event {
   private boolean lastCheckIsTimeOut = false;

   public VIPEvent(Player owner) {
      super(owner);
      this.checkrate = 1000;
   }

   public void work(long now) throws Exception {
      boolean timeOut = ((Player)this.getOwner()).getVIPManager().isTimeOut();
      if (timeOut && !this.lastCheckIsTimeOut) {
         ((Player)this.getOwner()).getVIPManager().onVIPTimeOut();
         this.setEnd(true);
      }

      this.lastCheckIsTimeOut = timeOut;
   }

   public Status getStatus() {
      return Status.VIP;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
