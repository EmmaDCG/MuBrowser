package com.mu.io.game.packet.imp.spiritOfWar;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class SpiritUseItem extends WriteOnlyPacket {
   public SpiritUseItem() {
      super(20609);
   }

   public static void sendToClient(Player player) {
      try {
         SpiritUseItem sui = new SpiritUseItem();
         sui.writeBoolean(true);
         player.writePacket(sui);
         sui.destroy();
         sui = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
