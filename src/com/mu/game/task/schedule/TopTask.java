package com.mu.game.task.schedule;

import com.mu.game.top.DungeonTopManager;
import com.mu.game.top.TopManager;
import com.mu.utils.concurrent.ThreadFixedPoolManager;

public class TopTask extends ScheduleTask {
   private static final int Inteval = 600000;

   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 600000L, 600000L);
   }

   public void doLocalTask() {
      TopManager.resetTop();
      DungeonTopManager.restBigdevilList();
   }

   public void doInterTask() {
      TopManager.resetTop();
      DungeonTopManager.restBigdevilList();
   }
}
