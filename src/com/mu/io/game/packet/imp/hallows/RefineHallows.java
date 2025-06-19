package com.mu.io.game.packet.imp.hallows;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class RefineHallows extends ReadAndWritePacket {
   public RefineHallows(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int result = player.getHallowsManager().refine();
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      } else {
         this.writeBoolean(true);
         player.writePacket(this);
      }

   }
}
