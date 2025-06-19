package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.io.game.packet.WriteOnlyPacket;

public class GangPlayerIn extends WriteOnlyPacket {
   public GangPlayerIn() {
      super(10611);
   }

   public static void pushPlayerIn(Gang gang, long rid) {
      try {
         GangPlayerIn gi = new GangPlayerIn();
         gi.writeDouble((double)rid);
         gang.broadcast(gi);
         gi.destroy();
         gi = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
