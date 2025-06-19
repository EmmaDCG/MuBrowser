package com.mu.io.game.packet.imp.dungeon;

import com.mu.io.game.packet.ReadAndWritePacket;

public class DungeonClickTiming extends ReadAndWritePacket {
   public DungeonClickTiming(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }
}
