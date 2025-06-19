package com.mu.io.game.packet.imp.pay;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class OpenBuyAPI extends WriteOnlyPacket {
   public OpenBuyAPI() {
      super(47001);
   }

   public static void sendToClient(Player player, String token, String urlParams) {
      try {
         OpenBuyAPI oba = new OpenBuyAPI();
         oba.writeUTF(token);
         oba.writeUTF(urlParams);
         player.writePacket(oba);
         oba.destroy();
         oba = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
