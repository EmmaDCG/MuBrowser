package com.mu.game.model.pet.ai;

import com.mu.game.model.map.Map;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.ai.AIState;
import com.mu.game.model.unit.player.Player;
import java.awt.Point;

public class MoveStatusHandler extends PetStateHandler {
   public void begin(PetAI aiCntl) {
      super.begin(aiCntl);
      aiCntl.getOwner().setLastAttackTarget((Creature)null);
      aiCntl.getOwner().getAggroList().clear();
      Pet pet = aiCntl.getOwner();
      Player player = pet.getOwner();
      Map map = player.getMap();
      Point petPosition = aiCntl.getOwner().getActualPosition();
      int s_x = petPosition.x;
      int s_y = petPosition.y;
      int at_x = player.getActualPosition().x;
      int at_y = player.getActualPosition().y;
      int at_dx = s_x - at_x;
      int at_dy = s_y - at_y;
      double at_dis = Math.sqrt((double)(at_dx * at_dx + at_dy * at_dy));
      int x = (int)((double)at_x + 1960.0D / at_dis * (double)at_dx);
      int y = (int)((double)at_y + 1960.0D / at_dis * (double)at_dy);
      Point moveTarget = aiCntl.searchFeasiblePoint(map, x, y, at_x, at_y);
      pet.startMove(new Point[]{new Point(s_x, s_y), moveTarget}, System.currentTimeMillis());
   }

   public void detect(PetAI aiCntl) {
      Pet pet = aiCntl.getOwner();
      Player player = pet.getOwner();
      Map map = player.getMap();
      Point petPosition = aiCntl.getOwner().getActualPosition();
      int s_x = petPosition.x;
      int s_y = petPosition.y;
      int at_x = player.getActualPosition().x;
      int at_y = player.getActualPosition().y;
      int at_dx = s_x - at_x;
      int at_dy = s_y - at_y;
      double at_dis = Math.sqrt((double)(at_dx * at_dx + at_dy * at_dy));
      if (at_dis >= 2010.0D) {
         int x = (int)((double)at_x + 2010.0D / at_dis * (double)at_dx);
         int y = (int)((double)at_y + 2010.0D / at_dis * (double)at_dy);
         Point moveTarget = aiCntl.searchFeasiblePoint(map, x, y, at_x, at_y);
         if (Math.abs(s_x - moveTarget.x) > 10 || Math.abs(s_y - moveTarget.y) > 10) {
            pet.startMove(new Point[]{new Point(s_x, s_y), moveTarget}, System.currentTimeMillis());
            return;
         }
      }

      aiCntl.startAIStateHandler(AIState.AS_STAND);
   }

   public AIState getState() {
      return AIState.AS_MOVE;
   }
}
