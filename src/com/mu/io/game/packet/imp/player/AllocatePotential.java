package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class AllocatePotential extends ReadAndWritePacket {
   public AllocatePotential(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int STR = this.readInt();
      int DEX = this.readInt();
      int INT = this.readInt();
      int CON = this.readInt();
      int result = PlayerManager.allocatePotential(player, STR, DEX, CON, INT);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }
}
