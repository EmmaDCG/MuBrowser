package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class RidingChange extends WriteOnlyPacket {
   public RidingChange() {
      super(10021);
   }

   public static void sendToClient(Player player) {
      try {
         RidingChange rc = new RidingChange();
         rc.writeDouble((double)player.getID());
         rc.writeBoolean(player.isInRiding());
         player.getMap().sendPacketToAroundPlayer(rc, player.getPosition());
         rc.destroy();
         rc = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
