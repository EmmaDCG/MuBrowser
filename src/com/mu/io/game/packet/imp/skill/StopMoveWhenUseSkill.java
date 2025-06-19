package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class StopMoveWhenUseSkill extends WriteOnlyPacket {
   public StopMoveWhenUseSkill() {
      super(30014);
   }

   public static void sendToClient(Player player) {
      try {
         StopMoveWhenUseSkill smwu = new StopMoveWhenUseSkill();
         smwu.writeDouble((double)player.getID());
         player.getMap().sendPacketToAroundPlayer(smwu, player, false);
         smwu.destroy();
         smwu = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
