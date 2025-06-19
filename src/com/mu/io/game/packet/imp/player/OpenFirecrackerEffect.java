package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class OpenFirecrackerEffect extends WriteOnlyPacket {
   public OpenFirecrackerEffect() {
      super(10022);
   }

   public static void pushEffect(Player player) {
      try {
         OpenFirecrackerEffect oe = new OpenFirecrackerEffect();
         oe.writeDouble((double)player.getID());
         player.getMap().sendPacketToAroundPlayer(oe, player, true);
         oe.destroy();
         oe = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
