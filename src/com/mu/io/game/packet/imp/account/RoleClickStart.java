package com.mu.io.game.packet.imp.account;

import com.mu.game.model.properties.levelData.PlayerLevelData;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;

public class RoleClickStart extends ReadAndWritePacket {
   public RoleClickStart(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (player.getLevel() == 1) {
         long exp = PlayerLevelData.getNeedExp(1);
         PlayerManager.addExp(player, exp, -1L);
      }

   }
}
