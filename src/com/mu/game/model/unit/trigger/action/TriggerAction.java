package com.mu.game.model.unit.trigger.action;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.trigger.action.model.TriggerModel;
import com.mu.utils.Rnd;

public abstract class TriggerAction {
   private int id;
   protected long lastTriggerTime;
   protected int duration;
   protected int rate;
   protected boolean metux;
   protected Creature owner;
   private boolean effect = true;

   public TriggerAction(int id) {
      this.id = id;
      TriggerModel model = TriggerModel.getModel(id);
      this.rate = model.getRate();
      this.metux = model.isMetux();
      this.duration = model.getDuration();
      this.lastTriggerTime = System.currentTimeMillis();
   }

   public abstract void handle(boolean var1, Object... var2) throws Exception;

   public abstract int getType();

   public abstract boolean privyCondition();

   public boolean meedCondition() {
      if (!this.isEffect()) {
         return false;
      } else {
         int rnd = Rnd.get(100000);
         if (rnd > this.getRate()) {
            return false;
         } else {
            return System.currentTimeMillis() - this.lastTriggerTime < (long)this.duration ? false : this.privyCondition();
         }
      }
   }

   public void destroy() {
      this.owner = null;
   }

   public boolean isEffect() {
      return this.effect;
   }

   public void setEffect(boolean effect) {
      this.effect = effect;
   }

   public int getID() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public boolean isMetux() {
      return this.metux;
   }

   public int getRate() {
      return this.rate;
   }

   public void setRate(int rate) {
      this.rate = rate;
   }

   public void setMetux(boolean metux) {
      this.metux = metux;
   }

   public Creature getOwner() {
      return this.owner;
   }

   public void setOwner(Creature owner) {
      this.owner = owner;
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public long getLastTriggerTime() {
      return this.lastTriggerTime;
   }

   public void setLastTriggerTime(long lastTriggerTime) {
      this.lastTriggerTime = lastTriggerTime;
   }
}
