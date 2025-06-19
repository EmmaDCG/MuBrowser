package com.mu.game.model.unit.ai.handler;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.ai.AI;
import com.mu.game.model.unit.ai.AIState;
import com.mu.game.model.unit.monster.Monster;
import com.mu.utils.geom.MathUtil;

public class SH_Attack implements IStateHandler {
   public String getName() {
      return "SH_Attack";
   }

   public AIState getState() {
      return AIState.AS_ATTACK;
   }

   public void begin(AI aiCntl, Object... objects) {
      Monster aiOwner = aiCntl.getOwner();
      Creature aiTarget = null;
      if (objects.length >= 1 && objects[0] instanceof Creature) {
         aiTarget = (Creature)objects[0];
         aiCntl.setCurStateCreature(aiTarget);
         if (!aiCntl.detectCurStateCreatureValid()) {
            aiOwner.getAggroList().remove(aiTarget);
            aiCntl.startAIStateHandler(AIState.AS_PURSUE);
            return;
         }
      } else {
         aiTarget = aiOwner.getAggroList().getMostHated();
         if (aiTarget == null) {
            aiCntl.startAIStateHandler(AIState.AS_GOHOME);
            return;
         }

         aiCntl.setCurStateCreature(aiTarget);
         if (!aiCntl.detectCurStateCreatureValid()) {
            aiOwner.getAggroList().remove(aiTarget);
            aiCntl.startAIStateHandler(AIState.AS_PURSUE);
            return;
         }
      }

      aiCntl.setCurStateCreature(aiTarget);
      aiOwner.attack(aiTarget);
   }

   public void detect(AI aiCntl) {
      Creature aiOwner = aiCntl.getOwner();
      Creature aiTarget = aiCntl.getCurStateCreature();
      if (!aiOwner.isAttacking()) {
         aiCntl.startAIStateHandler(AIState.AS_PURSUE);
      } else if (!aiCntl.detectCurStateCreatureValid()) {
         aiOwner.getAggroList().remove(aiTarget);
         aiCntl.startAIStateHandler(AIState.AS_PURSUE);
      } else {
         int distance = MathUtil.getDistance(aiOwner.getActualPosition(), aiTarget.getActualPosition());
         if (!aiCntl.isCurPositionForceAttack() && aiOwner.getMinAttackDistance() > distance || distance > aiOwner.getAttackDistance()) {
            aiCntl.startAIStateHandler(AIState.AS_PURSUE);
         }
      }
   }

   public void finish(AI aiCntl) {
      Creature aiOwner = aiCntl.getOwner();
      aiOwner.idle();
   }
}
