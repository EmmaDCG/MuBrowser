package com.mu.game.model.pet.ai;

import com.mu.game.model.unit.ai.AIState;

public abstract class PetStateHandler {
   public String getName() {
      return this.getClass().getSimpleName();
   }

   public abstract AIState getState();

   public void begin(PetAI aiCntl) {
      aiCntl.curStateBeginTime = System.currentTimeMillis();
   }

   public abstract void detect(PetAI var1);
}
