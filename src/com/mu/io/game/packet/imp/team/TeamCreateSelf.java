package com.mu.io.game.packet.imp.team;

import com.mu.game.model.team.TeamManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class TeamCreateSelf extends ReadAndWritePacket {
   public TeamCreateSelf(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      TeamManager.doOperation(1, player);
   }
}
