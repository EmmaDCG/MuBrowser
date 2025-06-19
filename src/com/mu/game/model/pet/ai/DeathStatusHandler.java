package com.mu.game.model.pet.ai;

import com.mu.game.model.unit.ai.AIState;

public class DeathStatusHandler extends PetStateHandler {
   public void begin(PetAI aiCntl) {
      super.begin(aiCntl);
   }

   public void detect(PetAI aiCntl) {
   }

   public AIState getState() {
      return AIState.AS_DEATH;
   }
}
