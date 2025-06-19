package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class TopCenterMessage extends WriteOnlyPacket {
   public TopCenterMessage(String msg) {
      super(1011);

      try {
         this.writeUTF(msg);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void writeMessage(Player player, String msg) {
      TopCenterMessage tm = new TopCenterMessage(msg);
      player.writePacket(tm);
      tm.destroy();
      tm = null;
   }
}
