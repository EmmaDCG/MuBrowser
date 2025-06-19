package com.mu.io.game.packet.imp.panda;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.io.IOException;

public class ExistPanda extends WriteOnlyPacket {
   public ExistPanda(boolean exist) {
      super(45001);

      try {
         this.writeBoolean(exist);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public static void sendMsgPandaExist(Player player, boolean exist) {
      ExistPanda packet = new ExistPanda(exist);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
