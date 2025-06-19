package com.mu.io.game.packet.imp.dungeon;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class DunEnterMoLian extends ReadAndWritePacket {
   public DunEnterMoLian(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int level = this.readByte();
      DungeonManager.createAndEnterDungeon(player, 11, Integer.valueOf(level));
   }
}
