package com.mu.game.model.unit.ai.handler;

import com.mu.game.model.unit.ai.AI;

public class SH_Stand_Attack extends SH_Stand {
   public String getName() {
      return "SH_Stand_Attack";
   }

   public void detect(AI aiCntl) {
      this.patrol(aiCntl);
   }
}
