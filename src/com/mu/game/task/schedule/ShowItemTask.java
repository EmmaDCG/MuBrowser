package com.mu.game.task.schedule;

import com.mu.game.model.item.ShowItemManager;
import com.mu.utils.concurrent.ThreadFixedPoolManager;

public class ShowItemTask extends ScheduleTask {
   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 300000L, 600000L);
   }

   public void doLocalTask() {
      ShowItemManager.checkExpire();
   }

   public void doInterTask() {
   }
}
