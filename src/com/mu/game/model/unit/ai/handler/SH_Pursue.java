package com.mu.game.model.unit.ai.handler;

import com.mu.game.model.map.AStar;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.ai.AI;
import com.mu.game.model.unit.ai.AIState;
import com.mu.game.model.unit.monster.Monster;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SH_Pursue implements IStateHandler {
   private static Logger logger = LoggerFactory.getLogger(SH_Pursue.class);

   public String getName() {
      return "SH_Pursue";
   }

   public AIState getState() {
      return AIState.AS_PURSUE;
   }

   public void begin(AI aiCntl, Object... objects) {
      Monster aiOwner = aiCntl.getOwner();
      Creature mostHated = aiOwner.getAggroList().getMostHated();
      if (mostHated == null) {
         aiCntl.startAIStateHandler(AIState.AS_GOHOME);
      } else {
         aiCntl.setCurStateCreature(mostHated);
         if (!aiCntl.detectCurStateCreatureValid()) {
            aiOwner.getAggroList().remove(mostHated);
            aiCntl.startAIStateHandler(AIState.AS_PURSUE);
         } else {
            double intervalDistance = (double)MathUtil.getDistance(aiOwner.getPosition(), mostHated.getPosition());
            if ((aiCntl.isCurPositionForceAttack() || (double)aiOwner.getMinAttackDistance() <= intervalDistance) && intervalDistance <= (double)aiOwner.getAttackDistance() && !aiOwner.getMap().isBlocked(aiOwner.getPosition().x, aiOwner.getPosition().y)) {
               aiCntl.startAIStateHandler(AIState.AS_ATTACK, mostHated);
            } else {
               Point purseOriginal = aiCntl.getPursueOriginalPosition();
               purseOriginal.setLocation(mostHated.getMoveTarget());
               Point pursuePoint = aiCntl.getAttackPosition(aiOwner.getPosition(), aiOwner.getAttackDistance(), aiOwner.getMinAttackDistance(), purseOriginal, mostHated);
               aiCntl.setCurStatePosition(pursuePoint);
               if (aiOwner.isFindWay()) {
                  ScheduledFuture future = AStar.pursue(aiOwner, mostHated, pursuePoint);
                  aiCntl.setMoveFuture(future);
               } else {
                  aiOwner.startMove(new Point[]{aiOwner.getActualPosition(), pursuePoint}, System.currentTimeMillis());
               }

            }
         }
      }
   }

   public void detect(AI aiCntl) {
      Monster aiOwner = aiCntl.getOwner();
      Creature aiTarget = aiCntl.getCurStateCreature();
      if (!aiCntl.detectCurStateCreatureValid()) {
         aiOwner.getAggroList().remove(aiTarget);
         aiCntl.startAIStateHandler(AIState.AS_PURSUE);
      } else {
         double bornDistance = (double)MathUtil.getDistance(aiOwner.getPosition(), aiOwner.getBornPoint());
         if (bornDistance > (double)aiOwner.getMaxMoveDistance()) {
            aiOwner.getAggroList().remove(aiTarget);
            aiCntl.startAIStateHandler(AIState.AS_PURSUE);
         } else {
            Point purseOriginal = aiCntl.getPursueOriginalPosition();
            if (!purseOriginal.equals(aiTarget.getMoveTarget())) {
               aiCntl.startAIStateHandler(AIState.AS_PURSUE);
            } else {
               double targetDistance = (double)MathUtil.getDistance(aiOwner.getPosition(), purseOriginal);
               if ((aiCntl.isCurPositionForceAttack() || (double)aiOwner.getMinAttackDistance() <= targetDistance) && targetDistance <= (double)aiOwner.getAttackDistance()) {
                  if (!aiOwner.getMap().isBlocked(aiOwner.getPosition().x, aiOwner.getPosition().y)) {
                     aiCntl.startAIStateHandler(AIState.AS_ATTACK, aiTarget);
                  }
               }
            }
         }
      }
   }

   public void finish(AI aiCntl) {
      ScheduledFuture future = aiCntl.getMoveFuture();
      if (future != null && !future.isCancelled()) {
         future.cancel(true);
      }

      aiCntl.setMoveFuture((ScheduledFuture)null);
   }
}
