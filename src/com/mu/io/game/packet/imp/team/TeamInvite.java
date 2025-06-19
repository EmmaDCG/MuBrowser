package com.mu.io.game.packet.imp.team;

import com.mu.game.CenterManager;
import com.mu.game.model.team.TeamManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class TeamInvite extends ReadAndWritePacket {
   public TeamInvite(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long id = (long)this.readDouble();
      if (id != player.getID()) {
         Player other = CenterManager.getPlayerByRoleID(id);
         if (other == null) {
            SystemMessage.writeMessage(player, 1021);
         } else if (TeamManager.isTeammate(player, other)) {
            SystemMessage.writeMessage(player, 19007);
         } else {
            TeamManager.doOperation(2, player, other);
         }
      }
   }
}
