package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class TopMessage extends WriteOnlyPacket {
   public TopMessage(String msg) {
      super(1010);

      try {
         this.writeUTF(msg);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void writeMessage(Player player, String msg) {
      TopMessage tm = new TopMessage(msg);
      player.writePacket(tm);
      tm.destroy();
      tm = null;
   }
}
