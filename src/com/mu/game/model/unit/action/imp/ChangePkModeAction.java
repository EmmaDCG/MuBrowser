package com.mu.game.model.unit.action.imp;

import com.mu.game.model.unit.action.Action;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.pkModel.ChangePkMode;

public class ChangePkModeAction implements Action {
   private int modelId;

   public ChangePkModeAction(int pkModeId) {
      this.modelId = pkModeId;
   }

   public void doAction(Player player) {
      ChangePkMode.change(player, this.modelId);
   }

   public void destroy() {
   }
}
