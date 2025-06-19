package com.mu.game.task.schedule;

import com.mu.utils.concurrent.ThreadFixedPoolManager;

public class CheckGangRedPacketTask extends ScheduleTask {
   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 180000L, 180000L);
   }

   public void doLocalTask() {
   }

   public void doInterTask() {
   }
}
