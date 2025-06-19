package com.mu.game.model.unit.monster.goldenboss;

import com.mu.game.task.specified.day.DailyTask;

public class GoldenBossTask extends DailyTask {
   public GoldenBossTask(int hour, int minute, int second) {
      super(hour, minute, second, 1);
   }

   public void doTask() throws Exception {
      GoldenBossManager.createBoss();
   }
}
