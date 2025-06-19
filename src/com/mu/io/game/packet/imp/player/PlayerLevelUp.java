package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class PlayerLevelUp extends WriteOnlyPacket {
   public PlayerLevelUp(long roleID) {
      super(10247);

      try {
         this.writeDouble((double)roleID);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendToClient(Player player) {
      PlayerLevelUp plu = new PlayerLevelUp(player.getID());
      player.getMap().sendPacketToAroundPlayer(plu, player, true);
      plu.destroy();
      plu = null;
   }
}
