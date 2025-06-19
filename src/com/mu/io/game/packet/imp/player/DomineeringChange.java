package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class DomineeringChange extends WriteOnlyPacket {
   public DomineeringChange() {
      super(10052);
   }

   public static void sendToClient(Player player, int changeValue) {
      try {
         DomineeringChange dc = new DomineeringChange();
         dc.writeInt(changeValue);
         player.writePacket(dc);
         dc.destroy();
         dc = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
