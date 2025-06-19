package com.mu.io.game.packet.imp.transfer;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.io.IOException;

public class TransferBroadcast extends WriteOnlyPacket {
   public TransferBroadcast(Player player) {
      super(44003);

      try {
         this.writeDouble((double)player.getID());
         this.writeByte(player.getProfessionID());
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }
}
