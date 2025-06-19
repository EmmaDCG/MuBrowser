package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.robot.Robot;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;

public class RobotAiEvent extends Event {
   public RobotAiEvent(Robot owner) {
      super(owner);
      this.checkrate = 2000;
   }

   public void work(long now) throws Exception {
      Robot robot = (Robot)this.getOwner();
      Status status = robot.getStatusEvent().getStatus();
      if (status.getIdentify() == Status.NONE.getIdentify()) {
         if (robot.getFollowTarget() != null && !robot.getFollowTarget().isDie() && !robot.getFollowTarget().isDestroy() && robot.getFollowTarget().isCanBeAttacked()) {
            robot.doFollow();
         } else {
            robot.searchTarget();
         }
      }

   }

   public Status getStatus() {
      return Status.RobotAi;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
