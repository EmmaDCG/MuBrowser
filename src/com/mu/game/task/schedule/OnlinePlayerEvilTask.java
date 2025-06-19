package com.mu.game.task.schedule;

import com.mu.config.VariableConstant;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.util.Iterator;

public class OnlinePlayerEvilTask extends ScheduleTask {
   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 60000L, 60000L);
   }

   public void doLocalTask() {
      long now = System.currentTimeMillis();
      this.check(now);
   }

   private void check(long now) {
      Iterator it = CenterManager.getAllPlayerMap().values().iterator();

      while(it.hasNext()) {
         Player p = (Player)it.next();
         if (p.getEvil() > 0 && !p.isNew() && now - p.getLoginTime() >= 1000L) {
            int newEvil = p.getEvil() - VariableConstant.Pk_Evil_Minute;
            p.changeEvil(newEvil);
         }
      }

   }

   public void doInterTask() {
      long now = System.currentTimeMillis();
      this.check(now);
   }
}
