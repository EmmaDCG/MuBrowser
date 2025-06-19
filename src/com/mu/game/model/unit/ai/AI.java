package com.mu.game.model.unit.ai;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.ai.handler.IStateHandler;
import com.mu.game.model.unit.ai.handler.SH_Attack;
import com.mu.game.model.unit.ai.handler.SH_BackHome;
import com.mu.game.model.unit.ai.handler.SH_BackHomeNoResume;
import com.mu.game.model.unit.ai.handler.SH_Death;
import com.mu.game.model.unit.ai.handler.SH_Pursue;
import com.mu.game.model.unit.ai.handler.SH_Stand;
import com.mu.game.model.unit.ai.handler.SH_Stand_Attack;
import com.mu.game.model.unit.ai.handler.SH_Stand_Attack_Patrol;
import com.mu.game.model.unit.ai.handler.SH_Stand_Patrol;
import com.mu.game.model.unit.monster.Monster;
import com.mu.utils.Rnd;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class AI {
   public static final int DIRECTION_SIZE = 16;
   public static final double DIRECTION_ANGLE = 0.39269908169872414D;
   public static final double[] DIRECTION_COS = new double[9];
   protected int id;
   protected Monster owner;
   protected Map stateHandlerMap = new HashMap();
   protected IStateHandler curStateHandler;
   protected long curStateBeginTime;
   protected Point curStatePosition = new Point();
   protected Point pursueOriginalPosition = new Point();
   protected Creature curStateCreature;
   protected boolean curStateFlag;
   protected boolean curPositionForceAttack;
   protected boolean destory = false;
   protected ScheduledFuture moveFuture;

   static {
      for(int i = 0; i < DIRECTION_COS.length; ++i) {
         DIRECTION_COS[i] = Math.cos(0.39269908169872414D * (double)i);
      }

   }

   protected AI(Monster creature, int id) {
      this.owner = creature;
      this.id = id;
   }

   public static AI newInstance(Monster creature, int id) {
      AI newAI = new AI(creature, id);
      switch(id) {
      case 1:
         newAI.registerStateHandler(new SH_Stand_Attack_Patrol());
         newAI.registerStateHandler(new SH_Pursue());
         newAI.registerStateHandler(new SH_Attack());
         newAI.registerStateHandler(new SH_BackHome());
         newAI.registerStateHandler(new SH_Death());
         break;
      case 2:
         newAI.registerStateHandler(new SH_Stand_Patrol());
         newAI.registerStateHandler(new SH_Pursue());
         newAI.registerStateHandler(new SH_Attack());
         newAI.registerStateHandler(new SH_BackHome());
         newAI.registerStateHandler(new SH_Death());
         break;
      case 3:
         newAI.registerStateHandler(new SH_Stand_Attack());
         newAI.registerStateHandler(new SH_Pursue());
         newAI.registerStateHandler(new SH_Attack());
         newAI.registerStateHandler(new SH_BackHome());
         newAI.registerStateHandler(new SH_Death());
         break;
      case 4:
         newAI.registerStateHandler(new SH_Stand());
         newAI.registerStateHandler(new SH_Attack());
         newAI.registerStateHandler(new SH_BackHome());
         newAI.registerStateHandler(new SH_Death());
      case 5:
      case 7:
      case 9:
      default:
         break;
      case 6:
         newAI.registerStateHandler(new SH_Stand());
         break;
      case 8:
         newAI.registerStateHandler(new SH_Stand());
         newAI.registerStateHandler(new SH_Death());
         break;
      case 10:
         newAI.registerStateHandler(new SH_Stand_Attack_Patrol());
         newAI.registerStateHandler(new SH_Pursue());
         newAI.registerStateHandler(new SH_Attack());
         newAI.registerStateHandler(new SH_BackHomeNoResume());
         newAI.registerStateHandler(new SH_Death());
      }

      newAI.startAIStateHandler(AIState.AS_STAND);
      return newAI;
   }

   private void registerStateHandler(IStateHandler sh) {
      this.stateHandlerMap.put(sh.getState(), sh);
   }

   public Monster getOwner() {
      return this.owner;
   }

   public int getID() {
      return this.id;
   }

   public IStateHandler getCurrentAIState() {
      return this.curStateHandler;
   }

   public boolean isBackHome() {
      return this.curStateHandler != null && this.curStateHandler.getState() == AIState.AS_GOHOME && !this.isCurStateFlag();
   }

   public synchronized void destroy() {
      if (!this.destory) {
         this.destory = true;
         this.stateHandlerMap.clear();
         this.stateHandlerMap = null;
         this.owner = null;
         this.curStateHandler = null;
         this.pursueOriginalPosition = null;
         this.curStatePosition = null;
         this.curStateCreature = null;
      }
   }

   public synchronized void startAIStateHandler(AIState state, Object... objects) {
      if (!this.destory) {
         if (!this.isDeath() && (!this.owner.isDie() || state == AIState.AS_DEATH)) {
            if (this.curStateHandler != null) {
               this.curStateHandler.finish(this);
               this.curStateHandler = null;
            }

            IStateHandler handler = (IStateHandler)this.stateHandlerMap.get(state);
            if (handler == null) {
               handler = (IStateHandler)this.stateHandlerMap.get(AIState.AS_STAND);
            }

            if (handler != null) {
               this.curStateBeginTime = System.currentTimeMillis();
               this.curStateFlag = false;
               this.curStateHandler = handler;
               this.curStateHandler.begin(this, objects);
            }
         }
      }
   }

   public boolean isDeath() {
      return this.curStateHandler != null && this.curStateHandler.getState() == AIState.AS_DEATH && this.owner.isDie();
   }

   public synchronized void deDingShen() {
      if (this.curStateHandler != null) {
         this.startAIStateHandler(this.curStateHandler.getState());
      }

   }

   public synchronized void detectAIStateHandler() {
      if (!this.destory) {
         if (this.curStateHandler != null) {
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

   public Point getCurStatePosition() {
      return this.curStatePosition;
   }

   public Creature getCurStateCreature() {
      return this.curStateCreature;
   }

   public static int recentAngleIndex(int vectorX, int vectorY) {
      double vectorCos = (double)vectorX / Math.sqrt((double)(vectorX * vectorX + vectorY * vectorY));
      int index = quickSeach(0, DIRECTION_COS.length - 1, vectorCos);
      return vectorY >= 0 ? index : 16 - index;
   }

   public static int quickSeach(int startIndex, int endIndex, double vectorCos) {
      if (endIndex - startIndex == 1) {
         return DIRECTION_COS[startIndex] - vectorCos > vectorCos - DIRECTION_COS[endIndex] ? endIndex : startIndex;
      } else {
         int intervalIndex = (endIndex - startIndex) / 2 + startIndex;
         if (DIRECTION_COS[intervalIndex] < vectorCos) {
            return quickSeach(startIndex, intervalIndex, vectorCos);
         } else {
            return DIRECTION_COS[intervalIndex] > vectorCos ? quickSeach(intervalIndex, endIndex, vectorCos) : intervalIndex;
         }
      }
   }

   public Point getAttackPosition(Point ownerPos, int maxAttackDistance, int minAttackDistance, Point targetPos, Creature target) {
      com.mu.game.model.map.Map map = this.owner.getMap();
      int diffx = ownerPos.x - targetPos.x;
      int diffy = ownerPos.y - targetPos.y;
      int index = recentAngleIndex(diffx, diffy);

      for(int i = 0; i < 3; ++i) {
         index = SurroundIndexFactory.getIndex(this.owner, target, index);
         double _side = (double)Rnd.get(Math.max(minAttackDistance, maxAttackDistance / 3), Math.max(minAttackDistance, maxAttackDistance - 5));
         double _angle = 0.39269908169872414D * (double)index;
         double _y = Math.sin(_angle) * _side;
         double _x = Math.cos(_angle) * _side;
         int x = (int)((double)targetPos.x + _x);
         int y = (int)((double)targetPos.y + _y);
         this.curPositionForceAttack = false;
         if (!map.isBlocked(x, y)) {
            return new Point(x, y);
         }
      }

      this.curPositionForceAttack = true;
      return map.isBlocked(targetPos.x, targetPos.y) ? map.searchFeasiblePoint(targetPos.x, targetPos.y) : new Point(targetPos.x, targetPos.y);
   }

   public boolean detectCurStateCreatureValid() {
      return !this.owner.isDie() && this.curStateCreature != null && !this.curStateCreature.isDestroy() && !this.curStateCreature.isDie() && this.owner.getMapID() == this.curStateCreature.getMapID();
   }

   public boolean isDestory() {
      return this.destory;
   }

   public boolean isCurStateFlag() {
      return this.curStateFlag;
   }

   public void setCurStateFlag(boolean partFinish) {
      this.curStateFlag = partFinish;
   }

   public void setCurStatePosition(Point curStatePosition) {
      this.curStatePosition.setLocation(curStatePosition);
   }

   public void setCurStateCreature(Creature curStateCreature) {
      this.curStateCreature = curStateCreature;
   }

   public void setCurStateBeginTime(long curStateBeginTime) {
      this.curStateBeginTime = curStateBeginTime;
   }

   public Point getPursueOriginalPosition() {
      return this.pursueOriginalPosition;
   }

   public void setPursueOriginalPosition(Point pursueOriginalPosition) {
      this.pursueOriginalPosition = pursueOriginalPosition;
   }

   public ScheduledFuture getMoveFuture() {
      return this.moveFuture;
   }

   public void setMoveFuture(ScheduledFuture moveFuture) {
      this.moveFuture = moveFuture;
   }

   public boolean isCurPositionForceAttack() {
      return this.curPositionForceAttack;
   }
}
