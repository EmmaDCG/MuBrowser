package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ZMDMessage extends WriteOnlyPacket {
   public ZMDMessage(String msg) {
      super(1012);

      try {
         this.writeUTF(msg);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void writeMessage(Player player, String msg) {
      ZMDMessage zm = new ZMDMessage(msg);
      player.writePacket(zm);
      zm.destroy();
      zm = null;
   }
}
