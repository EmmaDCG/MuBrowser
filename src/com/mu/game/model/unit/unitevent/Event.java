package com.mu.game.model.unit.unitevent;

import com.mu.game.model.unit.MapUnit;
import java.awt.Point;
import java.util.HashSet;

public abstract class Event {
   private MapUnit owner;
   protected boolean destroyed = false;
   protected boolean end = false;
   protected long lastCheckTime = 0L;
   protected int checkrate = 100;
   private HashSet limitOperations = new HashSet();

   public Event(MapUnit owner) {
      this.owner = owner;
      OperationLimitManager.addOperationLimit(this);
   }

   public MapUnit getOwner() {
      return this.owner;
   }

   public void doWork(long now) {
      if (now - this.lastCheckTime >= (long)this.checkrate) {
         try {
            this.work(now);
         } catch (Exception var7) {
            var7.printStackTrace();
         } finally {
            this.lastCheckTime = now;
         }
      }

   }

   public abstract void work(long var1) throws Exception;

   public abstract Status getStatus();

   public abstract OperationEnum getOperationEnum();

   public final boolean isDestroyed() {
      return this.destroyed;
   }

   public final boolean isEnd() {
      return this.end;
   }

   public final void setEnd(boolean end) {
      this.end = end;
   }

   public Point getActualPosition() {
      return this.owner.getPosition();
   }

   public HashSet getLimitOperations() {
      return this.limitOperations;
   }

   public void setLimitOperations(HashSet limitOperations) {
      this.limitOperations = limitOperations;
   }

   public void destroy() {
      if (!this.isDestroyed()) {
         this.getOwner().removeLimitOperation(2, this.getLimitOperations().toArray());
         this.limitOperations.clear();
         this.limitOperations = null;
         this.destroyed = true;
      }
   }
}
