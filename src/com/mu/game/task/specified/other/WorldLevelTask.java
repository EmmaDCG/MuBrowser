package com.mu.game.task.specified.other;

import com.mu.game.model.top.WorldLevelManager;
import com.mu.game.task.specified.SpecifiedTimeManager;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class WorldLevelTask extends TimerTask {
   private static final long interval = 1800000L;
   private Date executeDate = null;

   public void run() {
      WorldLevelManager.calculateWorldLevel();
   }

   public void start() {
      WorldLevelManager.calculateWorldLevel();
      Calendar calendar = Calendar.getInstance();
      calendar.add(12, 30);
      this.executeDate = calendar.getTime();
      SpecifiedTimeManager.schedule(this, this.executeDate, 1800000L);
   }
}
