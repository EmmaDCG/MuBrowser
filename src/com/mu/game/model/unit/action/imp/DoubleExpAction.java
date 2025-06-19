package com.mu.game.model.unit.action.imp;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.imp.test.ActivityTest;
import com.mu.game.model.unit.action.Action;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Time;

public class DoubleExpAction implements Action {
   public void doAction(Player player) {
      ActivityTest activity = ActivityManager.getActivityTest();
      if (activity != null && activity.isOpen()) {
         if (!player.getBuffManager().hasBuff(activity.getBuffId())) {
            long tmp = Time.getTodayBegin() + (long)(activity.getStartHour() * 3600) * 1000L + (long)(activity.getStartMinute() * 60) * 1000L + activity.getBuffDuration() - System.currentTimeMillis();
            if (tmp > 5000L) {
               player.getBuffManager().createAndStartBuff(player, activity.getBuffId(), 1, true, tmp);
            }

         }
      }
   }

   public void destroy() {
   }
}
