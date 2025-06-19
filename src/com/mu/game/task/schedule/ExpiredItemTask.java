package com.mu.game.task.schedule;

import com.mu.game.CenterManager;
import com.mu.game.model.item.operation.ItemManager;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.util.Iterator;

public class ExpiredItemTask extends ScheduleTask {
   private static final int expiredInteval = 30000;

   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 30000L, 30000L);
   }

   public void doLocalTask() {
      long now = System.currentTimeMillis();
      this.check(now);
   }

   private void check(long now) {
      Iterator it = CenterManager.getAllPlayerMap().values().iterator();

      while(it.hasNext()) {
         Player p = (Player)it.next();

         try {
            if (!p.isDestroy()) {
               ItemManager manager = p.getItemManager();
               if (manager != null) {
                  manager.handleExpiredItem(false);
               }
            }
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

   }

   public void doInterTask() {
      long now = System.currentTimeMillis();
      this.check(now);
   }
}
