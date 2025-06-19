package com.mu.game.model.unit.ai.handler;

import com.mu.game.model.unit.ai.AI;
import com.mu.game.model.unit.ai.AIState;
import com.mu.game.model.unit.monster.Monster;
import com.mu.utils.Tools;
import java.awt.Point;

public class SH_Stand implements IStateHandler {
   public String getName() {
      return "SH_Stand";
   }

   public AIState getState() {
      return AIState.AS_STAND;
   }

   public void begin(AI aiCntl, Object... objects) {
      aiCntl.setCurStateFlag(true);
   }

   public void detect(AI aiCntl) {
   }

   public void finish(AI aiCntl) {
   }

   protected boolean patrol(AI aiCntl) {
      long cur = System.currentTimeMillis();
      Monster creature = aiCntl.getOwner();
      if (cur - creature.getLastPatrolTime() >= 3000L) {
         if (creature.hasEnemyAround()) {
            aiCntl.startAIStateHandler(AIState.AS_PURSUE);
            return true;
         }

         creature.setLastPatrolTime(cur);
      }

      return false;
   }

   protected boolean walk(AI aiCntl) {
      Monster creature = aiCntl.getOwner();
      long currentInterval = aiCntl.getCurStateRuningTime();
      int walkInterval = creature.getWalkInterval();
      if (currentInterval >= (long)walkInterval) {
         Point targetPoint = aiCntl.getCurStatePosition();
         if (aiCntl.isCurStateFlag()) {
            if (!creature.hasWalkRoutes()) {
               return false;
            }

            Point moveTarget = creature.getWalkTarget();
            if (creature.getMap().isBlocked(moveTarget.x, moveTarget.y)) {
               return false;
            }

            aiCntl.setCurStateFlag(false);
            aiCntl.setCurStatePosition(moveTarget);
            creature.startMove(new Point[]{new Point(creature.getActualPosition()), moveTarget}, System.currentTimeMillis());
            moveTarget = null;
         } else if (Tools.isIntervalLittle(creature.getPosition(), targetPoint, 2.0D)) {
            aiCntl.setCurStateFlag(true);
            return false;
         }

         return true;
      } else {
         return false;
      }
   }
}
