package com.mu.io.game.packet.imp.player;

import com.mu.io.game.packet.ReadAndWritePacket;

public class InitBossTelTime extends ReadAndWritePacket {
   public InitBossTelTime(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }
}
