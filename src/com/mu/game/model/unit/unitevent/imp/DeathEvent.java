package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;

public class DeathEvent extends Event {
   public DeathEvent(Creature owner) {
      super(owner);
   }

   public void work(long now) throws Exception {
   }

   public Status getStatus() {
      return Status.DEATH;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
