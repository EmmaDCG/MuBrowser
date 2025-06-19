package com.mu.game.task.specified.day;

import com.mu.game.CenterManager;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.imp.test.ActivityTest;
import com.mu.game.model.unit.action.DelayAction;
import com.mu.game.model.unit.action.imp.DoubleExpAction;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.map.EnterMapSuccess;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.util.Iterator;

public class TestBuffStartTask extends DailyTask {
   public TestBuffStartTask() {
      ActivityTest activity = ActivityManager.getActivityTest();
      if (activity != null) {
         this.hour = activity.getStartHour();
         this.minute = activity.getStartMinute();
      }

   }

   public void doTask() throws Exception {
      ActivityTest activity = ActivityManager.getActivityTest();
      if (activity != null && activity.isOpen()) {
         long interval = activity.getBuffDuration();
         final DelayAction da = new DelayAction(new DoubleExpAction(), 50L);
         EnterMapSuccess.addGlobalEnterMapAction(da);
         ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
            public void run() {
               EnterMapSuccess.removeGlobalEnterMapAction(da);
            }
         }, interval);
         Iterator it = CenterManager.getAllPlayerIterator();

         while(it.hasNext()) {
            Player player = (Player)it.next();

            try {
               player.getBuffManager().createAndStartBuff(player, activity.getBuffId(), 1, true, interval);
            } catch (Exception var8) {
               var8.printStackTrace();
            }
         }
      }

   }
}
