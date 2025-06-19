package com.mu.game.model.task.target;

import com.mu.game.model.task.Task;

public class TaskTargetRate {
   private int index;
   private Task task;
   private TaskTarget target;
   private int curRate;
   private int maxRate;
   private boolean forceComplete;
   private boolean destroy;

   public TaskTargetRate(Task task, TaskTarget target) {
      this.index = target.getIndex();
      this.maxRate = target.getMaxRate();
      this.target = target;
      this.task = task;
   }

   public int addRate() {
      return this.addRate(1);
   }

   public int addRate(int inc) {
      return this.setRate(this.curRate + inc);
   }

   public int setRate(int rate) {
      rate = Math.max(0, rate);
      rate = Math.min(this.maxRate, rate);
      return this.curRate = rate;
   }

   public boolean ok() {
      return this.forceComplete || this.maxRate == this.curRate;
   }

   public int getIndex() {
      return this.index;
   }

   public Task getTask() {
      return this.task;
   }

   public int getCurRate() {
      return this.curRate;
   }

   public int getMaxRate() {
      return this.maxRate;
   }

   public void destroy() {
      if (!this.destroy) {
         this.destroy = true;
         this.target = null;
         this.task = null;
      }
   }

   public void forceComplete() {
      this.forceComplete = true;
      this.curRate = this.maxRate;
   }

   public boolean isForceComplete() {
      return this.forceComplete;
   }

   public TaskTarget getTarget() {
      return this.target;
   }

   public void clear() {
      this.forceComplete = false;
      this.curRate = 0;
   }

   public int getRealRate() {
      return Math.max(this.curRate, this.target.getRealRate(this.task.getOwner()));
   }
}
