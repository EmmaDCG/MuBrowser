package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.io.game.packet.WriteOnlyPacket;

public class RedPacketLeft extends WriteOnlyPacket {
   public RedPacketLeft() {
      super(10641);
   }

   public static void pushLeft(Gang gang, long rid, int size) {
      try {
         RedPacketLeft pl = new RedPacketLeft();
         pl.writeDouble((double)rid);
         pl.writeByte(size);
         gang.broadcast(pl);
         pl.destroy();
         pl = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
