package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.controller.CountdownCtller;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;

public class LeaveGearEvent extends CountDownEvent {
   public LeaveGearEvent(Player owner, CountdownCtller cc) {
      super(owner, cc);
   }

   public void work(long now) throws Exception {
      super.work(now);
   }

   public Status getStatus() {
      return Status.LEAVEGEAR;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
