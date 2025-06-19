package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ReceiveTransactionRequest extends WriteOnlyPacket {
   public ReceiveTransactionRequest() {
      super(13002);
   }

   public static void receiveRequest(long initiatorId, String initiatorName, long time, Player targetPlayer) {
      try {
         ReceiveTransactionRequest rt = new ReceiveTransactionRequest();
         rt.writeDouble((double)initiatorId);
         rt.writeUTF(initiatorName);
         targetPlayer.writePacket(rt);
         rt.destroy();
         rt = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
