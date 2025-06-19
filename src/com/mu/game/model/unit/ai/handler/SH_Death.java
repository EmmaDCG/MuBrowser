package com.mu.game.model.unit.ai.handler;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.ai.AI;
import com.mu.game.model.unit.ai.AIState;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;

public class SH_Death implements IStateHandler {
   public String getName() {
      return "SH_Death";
   }

   public AIState getState() {
      return AIState.AS_DEATH;
   }

   public void begin(AI aiCntl, Object... objects) {
      Monster aiOwner = aiCntl.getOwner();
      aiCntl.setCurStateFlag(true);
      if (aiOwner.getRevivalTime() < 0L) {
         aiCntl.setCurStateFlag(false);
      } else {
         long revivalTime = System.currentTimeMillis() + aiOwner.getRevivalTime();
         aiCntl.setCurStateBeginTime(revivalTime);
      }
   }

   public void detect(AI aiCntl) {
      if (aiCntl.isCurStateFlag()) {
         long revivalTime = aiCntl.getCurStateBeginTime();
         if (System.currentTimeMillis() >= revivalTime) {
            this.revival(aiCntl);
         }
      }

   }

   public void finish(AI aiCntl) {
   }

   private void revival(AI aiCntl) {
      aiCntl.setCurStateFlag(false);
      Creature creature = aiCntl.getOwner();
      creature.revival();
      aiCntl.startAIStateHandler(AIState.AS_STAND);

      try {
         Rectangle area = creature.getMap().getArea(creature.getPosition());
         if (area == null) {
            return;
         }

         Iterator it = creature.getMap().getPlayerMap().values().iterator();

         while(it.hasNext()) {
            Player player = (Player)it.next();
            if (!player.isDestroy()) {
               Point p = player.getPosition();
               if (area.contains(p) && player.isEnterMap()) {
                  WriteOnlyPacket aroundPacket = creature.createAroundSelfPacket(player);
                  player.writePacket(aroundPacket);
                  aroundPacket.destroy();
                  aroundPacket = null;
               }
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      this.sendBuff(creature);
   }

   private void sendBuff(Creature creature) {
   }
}
