package com.mu.game.task.schedule;

import com.mu.utils.concurrent.ThreadFixedPoolManager;

public class HttpHeartbeat extends ScheduleTask {
   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 5000L, 20000L);
   }

   public void doLocalTask() {
   }

   public void doInterTask() {
      this.doLocalTask();
   }
}
