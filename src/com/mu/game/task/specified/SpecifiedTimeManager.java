package com.mu.game.task.specified;

import com.mu.config.Global;
import com.mu.game.task.specified.day.BigDevilTopReceiveTask;
import com.mu.game.task.specified.day.ZeroDailyTask;
import com.mu.game.task.specified.other.WorldLevelTask;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SpecifiedTimeManager {
   private static Timer timer = new Timer();

   public static final void schedule(TimerTask task, Date date) {
      timer.schedule(task, date);
   }

   public static final void schedule(TimerTask task, Date firstDate, long period) {
      timer.scheduleAtFixedRate(task, firstDate, period);
   }

   public static final int purge() {
      return timer.purge();
   }

   public static final void start() {
      try {
         ZeroDailyTask st = new ZeroDailyTask();
         st.start();
         if (!Global.isInterServiceServer()) {
            WorldLevelTask wt = new WorldLevelTask();
            wt.start();
            BigDevilTopReceiveTask bt = new BigDevilTopReceiveTask();
            bt.start();
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
