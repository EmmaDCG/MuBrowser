package com.mu.io.game.packet.imp.storage;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class DeportCount extends WriteOnlyPacket {
   public DeportCount() {
      super(20010);
   }

   public DeportCount(int maxCount, int currentCount) {
      super(20010);

      try {
         this.writeByte(maxCount);
         this.writeByte(currentCount);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendToClient(Player player, int maxCount, int currentCount) {
      DeportCount dc = new DeportCount(maxCount, currentCount);
      player.writePacket(dc);
      dc.destroy();
      dc = null;
   }
}
