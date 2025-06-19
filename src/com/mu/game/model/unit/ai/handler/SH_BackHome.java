package com.mu.game.model.unit.ai.handler;

import com.mu.game.model.unit.ai.AI;
import com.mu.game.model.unit.ai.AIState;
import com.mu.game.model.unit.monster.Monster;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;

public class SH_BackHome implements IStateHandler {
   public String getName() {
      return "SH_BackHome";
   }

   public AIState getState() {
      return AIState.AS_GOHOME;
   }

   public void begin(AI aiCntl, Object... objects) {
      Monster aiOwner = aiCntl.getOwner();
      aiOwner.getAggroList().clear();
      Point born = aiOwner.getBornPoint();
      double bornDistance = (double)MathUtil.getDistance(aiOwner.getPosition(), aiOwner.getBornPoint());
      if (bornDistance <= 3.0D) {
         aiCntl.setCurStateFlag(true);
         aiOwner.fullResume();
         aiOwner.getAggroList().clear();
         aiCntl.startAIStateHandler(AIState.AS_STAND);
      } else {
         aiCntl.setCurStatePosition(born);
         aiOwner.startMove(new Point[]{new Point(aiOwner.getActualPosition()), new Point(born)}, System.currentTimeMillis());
      }

   }

   public void detect(AI aiCntl) {
      Monster aiOwner = aiCntl.getOwner();
      double bornDistance = (double)MathUtil.getDistance(aiOwner.getPosition(), aiOwner.getBornPoint());
      if (!aiOwner.isMoving() || bornDistance <= 3.0D) {
         aiCntl.setCurStateFlag(true);
         aiOwner.fullResume();
         aiOwner.getAggroList().clear();
         aiCntl.startAIStateHandler(AIState.AS_STAND);
      }

   }

   public void finish(AI aiCntl) {
   }
}
