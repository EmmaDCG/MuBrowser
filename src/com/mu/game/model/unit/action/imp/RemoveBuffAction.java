package com.mu.game.model.unit.action.imp;

import com.mu.game.model.unit.action.Action;
import com.mu.game.model.unit.player.Player;

public class RemoveBuffAction implements Action {
   private int buffId;

   public RemoveBuffAction(int buffId) {
      this.buffId = buffId;
   }

   public void doAction(Player player) {
      player.getBuffManager().endBuff(this.buffId, true);
   }

   public void destroy() {
   }
}
