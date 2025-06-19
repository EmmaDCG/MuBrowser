package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class StopCountDown extends WriteOnlyPacket {
   public StopCountDown() {
      super(10016);
   }

   public static void stop(Player player) {
      try {
         StopCountDown sd = new StopCountDown();
         sd.writeBoolean(true);
         player.writePacket(sd);
         sd.destroy();
         sd = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
