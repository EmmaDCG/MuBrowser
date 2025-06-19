package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class PutMoneyByServer extends WriteOnlyPacket {
   public PutMoneyByServer() {
      super(13013);
   }

   public static void putMoneyByServer(Player owner, Player target, int ingot) {
      try {
         PutMoneyByServer ps = new PutMoneyByServer();
         ps.writeInt(ingot);
         target.writePacket(ps);
         ps.destroy();
         ps = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
