package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.io.game.packet.WriteOnlyPacket;

public class SomeOneSendPacket extends WriteOnlyPacket {
   public SomeOneSendPacket() {
      super(10642);
   }

   public static void send(Gang gang, long rid) {
      try {
         SomeOneSendPacket sp = new SomeOneSendPacket();
         sp.writeDouble((double)rid);
         gang.broadcast(sp);
         sp.destroy();
         sp = null;
         gang.broadcastRedPacketSize();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
