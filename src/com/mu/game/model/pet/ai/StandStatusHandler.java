package com.mu.game.model.pet.ai;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.ai.AIState;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;

public class StandStatusHandler extends PetStateHandler {
   public void begin(PetAI aiCntl) {
      super.begin(aiCntl);
      aiCntl.getOwner().setLastAttackTarget((Creature)null);
      aiCntl.getOwner().getAggroList().clear();
   }

   public void detect(PetAI aiCntl) {
      Player player = aiCntl.getOwner().getOwner();
      Point playerPosition = player.getActualPosition();
      Point petPosition = aiCntl.getOwner().getActualPosition();
      int s_x = petPosition.x;
      int s_y = petPosition.y;
      double distance = MathUtil.getDistance((double)playerPosition.x, (double)playerPosition.y, (double)petPosition.x, (double)petPosition.y);
      Map map = player.getMap();
      if (distance >= 2010.0D) {
         aiCntl.startAIStateHandler(AIState.AS_MOVE);
      } else {
         Creature target = aiCntl.getOwner().getLastAttackTarget();
         if (target != null && !target.isDie()) {
            int at_x = target.getPosition().x;
            int at_y = target.getPosition().y;
            int at_dx = s_x - at_x;
            int at_dy = s_y - at_y;
            double at_dis = Math.sqrt((double)(at_dx * at_dx + at_dy * at_dy));
            if (at_dis >= 1500.0D) {
               int x = (int)((double)at_x + 1450.0D / at_dis * (double)at_dx);
               int y = (int)((double)at_y + 1450.0D / at_dis * (double)at_dy);
               Point moveTarget = aiCntl.searchFeasiblePoint(map, x, y, at_x, at_y);
               if (Math.abs(s_x - moveTarget.x) <= 3 && Math.abs(s_y - moveTarget.y) <= 3) {
                  aiCntl.startAIStateHandler(AIState.AS_ATTACK);
               } else {
                  aiCntl.startAIStateHandler(AIState.AS_PURSUE);
               }
            } else {
               aiCntl.startAIStateHandler(AIState.AS_ATTACK);
            }

         }
      }
   }

   public AIState getState() {
      return AIState.AS_STAND;
   }
}
