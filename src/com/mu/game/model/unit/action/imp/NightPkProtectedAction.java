package com.mu.game.model.unit.action.imp;

import com.mu.game.model.unit.action.Action;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Time;

public class NightPkProtectedAction implements Action {
   public void doAction(Player player) {
      if (!player.getBuffManager().hasBuff(80008)) {
         long nightProtectedInterval = (long)BuffModel.getModel(80008).getIntervalTime();
         long tmp = Time.getTodayBegin() + nightProtectedInterval - System.currentTimeMillis();
         if (tmp > 5000L) {
            player.getBuffManager().createAndStartBuff(player, 80008, 1, true, tmp);
         }

      }
   }

   public void destroy() {
   }
}
