package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class WashPotential extends ReadAndWritePacket {
   public WashPotential(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int result = PlayerManager.washPotential(player, true);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }
}
