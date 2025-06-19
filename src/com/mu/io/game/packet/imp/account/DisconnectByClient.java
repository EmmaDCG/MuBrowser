package com.mu.io.game.packet.imp.account;

import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class DisconnectByClient extends ReadAndWritePacket {
   public DisconnectByClient(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public DisconnectByClient() {
      super(107, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (player != null) {
         player.setShouldDestroy(true);
      } else {
         CenterManager.clearNotInPlayer(this.getChannel());
      }

   }
}
