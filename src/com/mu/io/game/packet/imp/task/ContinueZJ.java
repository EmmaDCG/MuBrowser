package com.mu.io.game.packet.imp.task;

import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ContinueZJ extends ReadAndWritePacket {
   public ContinueZJ(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      PlayerTaskManager ptm = null;
      if (player == null || player.getTaskManager() == null) {
         ;
      }
   }
}
