package com.mu.game.model.unit.ai.handler;

import com.mu.game.model.unit.ai.AI;

public class SH_Stand_Attack_Patrol extends SH_Stand {
   public String getName() {
      return "SH_Stand_Attack_Patrol";
   }

   public void detect(AI aiCntl) {
      if (!this.patrol(aiCntl)) {
         this.walk(aiCntl);
      }

   }
}
