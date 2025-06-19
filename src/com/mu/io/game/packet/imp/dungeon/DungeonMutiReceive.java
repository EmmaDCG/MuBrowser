package com.mu.io.game.packet.imp.dungeon;

import com.mu.io.game.packet.ReadAndWritePacket;

public class DungeonMutiReceive extends ReadAndWritePacket {
   public DungeonMutiReceive(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public DungeonMutiReceive() {
      super(12016, (byte[])null);
   }

   public void process() throws Exception {
   }
}
