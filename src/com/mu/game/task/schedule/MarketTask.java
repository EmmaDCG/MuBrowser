package com.mu.game.task.schedule;

import com.mu.game.model.market.MarketManager;
import com.mu.utils.concurrent.ThreadFixedPoolManager;

public class MarketTask extends ScheduleTask {
   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 60000L, 60000L);
   }

   public void doLocalTask() {
      MarketManager.checkExpired();
   }

   public void doInterTask() {
      this.doLocalTask();
   }
}
