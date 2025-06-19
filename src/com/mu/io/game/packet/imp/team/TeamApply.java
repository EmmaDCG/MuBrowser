package com.mu.io.game.packet.imp.team;

import com.mu.game.model.team.TeamManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class TeamApply extends ReadAndWritePacket {
   public TeamApply(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public TeamApply() {
      super(11002, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int teamId = this.readInt();
      TeamManager.doOperation(3, player, teamId);
   }
}
