package com.mu.game.model.unit.action.imp;

import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.unit.action.Action;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class PushPacketAction implements Action {
   public WriteOnlyPacket packet = null;
   private int functionId = -1;

   public PushPacketAction(WriteOnlyPacket packet) {
      this.packet = packet;
   }

   public void setFunctionId(int functionId) {
      this.functionId = functionId;
   }

   public void doAction(Player player) {
      if (this.functionId < 0 || FunctionOpenManager.isOpen(player, this.functionId)) {
         player.writePacket(this.packet);
      }

   }

   public void destroy() {
      this.packet.destroy();
   }
}
