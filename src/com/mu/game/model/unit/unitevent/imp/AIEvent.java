package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.ai.AI;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIEvent extends Event {
   private static Logger logger = LoggerFactory.getLogger(AIEvent.class);

   public AIEvent(Monster owner) {
      super(owner);
      this.checkrate = 800;
   }

   public Status getStatus() {
      return Status.AI;
   }

   public void work(long now) throws Exception {
      AI ai = ((Monster)this.getOwner()).getAI();
      if (ai != null) {
         ai.detectAIStateHandler();
      }

   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
