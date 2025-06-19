package com.mu.io.game.packet.imp.sys;

import com.mu.config.Global;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class RequestPay extends WriteOnlyPacket {
   public RequestPay() {
      super(1008);
   }

   public static void pushPayUrl(Player player) {
      RequestPay rp = new RequestPay();

      try {
         rp.writeUTF(Global.getPayUrl(player));
         player.writePacket(rp);
         rp.destroy();
         rp = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
