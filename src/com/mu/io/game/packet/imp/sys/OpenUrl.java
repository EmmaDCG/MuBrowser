package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class OpenUrl extends WriteOnlyPacket {
   public OpenUrl() {
      super(1001);
   }

   public static void open(Player player, String url) {
      try {
         OpenUrl ou = new OpenUrl();
         ou.writeUTF(url);
         player.writePacket(ou);
         ou.destroy();
         ou = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
