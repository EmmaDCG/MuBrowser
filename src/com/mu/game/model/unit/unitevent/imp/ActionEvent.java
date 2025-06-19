package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;

public class ActionEvent extends Event {
   private Runnable run;

   public ActionEvent(Player owner, Runnable run) {
      super(owner);
      this.run = run;
   }

   public void work(long now) throws Exception {
      if (this.run != null) {
         this.run.run();
      }

      this.setEnd(true);
   }

   public Status getStatus() {
      return Status.Action;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
