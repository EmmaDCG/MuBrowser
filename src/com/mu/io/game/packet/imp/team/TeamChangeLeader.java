package com.mu.io.game.packet.imp.team;

import com.mu.game.model.team.TeamManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class TeamChangeLeader extends ReadAndWritePacket {
   public TeamChangeLeader(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long id = (long)this.readDouble();
      TeamManager.doOperation(7, player, id);
   }
}
