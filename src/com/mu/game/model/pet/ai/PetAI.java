package com.mu.game.model.pet.ai;

import com.mu.game.model.pet.Pet;
import com.mu.game.model.unit.ai.AIState;
import com.mu.io.game.packet.imp.attack.CreaturePositionCorrect;
import com.mu.utils.Rnd;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

public class PetAI {
   public static final int Interval_Poll = 200;
   public static final int FOLLOW_DISTANCE = 2000;
   public static final int Distance_From_Player = 2010;
   public static final int Distance_Attack = 1500;
   protected Pet owner;
   protected Map stateHandlerMap = new HashMap();
   protected PetStateHandler curStateHandler;
   protected long curStateBeginTime;
   protected long lastPollTime;
   protected boolean curPositionForceAttack;
   protected boolean destory = false;

   protected PetAI(Pet creature) {
      this.owner = creature;
   }

   public static PetAI newInstance(Pet creature) {
      PetAI newAI = new PetAI(creature);
      newAI.registerStateHandler(new StandStatusHandler());
      newAI.registerStateHandler(new MoveStatusHandler());
      newAI.registerStateHandler(new AttackStatusHandler());
      newAI.registerStateHandler(new PursueStatusHandler());
      newAI.startAIStateHandler(AIState.AS_STAND);
      return newAI;
   }

   private void registerStateHandler(PetStateHandler sh) {
      this.stateHandlerMap.put(sh.getState(), sh);
   }

   public Pet getOwner() {
      return this.owner;
   }

   public PetStateHandler getCurrentAIState() {
      return this.curStateHandler;
   }

   public void destroy() {
      if (!this.destory) {
         this.destory = true;
         this.stateHandlerMap.clear();
         this.stateHandlerMap = null;
         this.owner = null;
         this.curStateHandler = null;
      }
   }

   public void startAIStateHandler(AIState state) {
      if (!this.destory) {
         PetStateHandler handler = (PetStateHandler)this.stateHandlerMap.get(state);
         if (handler == null) {
            handler = (PetStateHandler)this.stateHandlerMap.get(AIState.AS_STAND);
         }

         if (handler != null) {
            this.curStateHandler = handler;
            this.curStateHandler.begin(this);
         }
      }
   }

   public boolean isDeath() {
      return this.curStateHandler != null && this.curStateHandler.getState() == AIState.AS_DEATH && this.owner.isDie();
   }

   public void detectAIStateHandler(long now) {
      if (!this.destory) {
         if (now - this.lastPollTime >= 200L) {
            this.lastPollTime = now;
            this.curStateHandler.detect(this);
         }
      }
   }

   public long getCurStateBeginTime() {
      return this.curStateBeginTime;
   }

   public long getCurStateRuningTime() {
      return System.currentTimeMillis() - this.getCurStateBeginTime();
   }

   public boolean isDestory() {
      return this.destory;
   }

   public void setCurStateBeginTime(long curStateBeginTime) {
      this.curStateBeginTime = curStateBeginTime;
   }

   public boolean isCurPositionForceAttack() {
      return this.curPositionForceAttack;
   }

   public Point rndPosition(com.mu.game.model.map.Map map, int x, int y) {
      if (map == null) {
         return new Point(x, y);
      } else {
         int maxX = Math.max(map.getLeft(), x + 2000);
         int minX = Math.min(map.getRight(), x - 2000);
         int maxY = Math.max(map.getBottom(), y + 2000);
         int minY = Math.min(map.getTop(), y - 2000);

         for(int i = 0; i < 3; ++i) {
            int rndX = Rnd.get(minX, maxX);
            int rndY = Rnd.get(minY, maxY);
            if (!map.isBlocked(rndX, rndY)) {
               return new Point(rndX, rndY);
            }
         }

         return new Point(x, y);
      }
   }

   public void dragTo(int x, int y) {
      Rectangle newArea = this.owner.getMap().getArea(x, y);
      Rectangle oldArea = this.owner.getArea();
      this.owner.setPosition(x, y);
      if (newArea != null && !newArea.equals(oldArea)) {
         this.owner.switchArea(newArea, oldArea);
      }

      CreaturePositionCorrect.correntWhenTeleport(this.owner, x, y);
   }

   public Point searchFeasiblePoint(com.mu.game.model.map.Map map, int s_x, int s_y, int e_x, int e_y) {
      if (!map.isBlocked(s_x, s_y)) {
         return new Point(s_x, s_y);
      } else {
         s_x = map.getTileX(s_x);
         s_y = map.getTileY(s_y);
         e_x = map.getTileX(e_x);
         e_y = map.getTileY(e_y);
         int xu = s_x < e_x ? 1 : -1;
         int yu = s_y < e_y ? 1 : -1;
         int max_x = Math.abs(e_x - s_x);
         int max_y = Math.abs(e_y - s_y);
         int max = Math.max(max_x, max_y);

         for(int i = 1; i < max; ++i) {
            int x = s_x + Math.min(i, max_x) * xu;
            int y = s_y + Math.min(i, max_y) * yu;
            if (!map.tileIsBlocked(x, y)) {
               return new Point(map.getXByTile(x), map.getYByTile(y));
            }
         }

         return new Point(map.getXByTile(e_x), map.getYByTile(e_y));
      }
   }
}
