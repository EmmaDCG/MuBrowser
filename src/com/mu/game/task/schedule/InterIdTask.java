package com.mu.game.task.schedule;

import com.mu.utils.concurrent.ThreadFixedPoolManager;

public class InterIdTask extends ScheduleTask {
   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 1200000L, 1200000L);
   }

   public void doLocalTask() {
   }

   public void doInterTask() {
   }
}
