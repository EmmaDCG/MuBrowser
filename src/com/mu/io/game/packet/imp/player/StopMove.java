package com.mu.io.game.packet.imp.player;

import com.mu.io.game.packet.ReadAndWritePacket;

public class StopMove extends ReadAndWritePacket {
   public StopMove(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }
}
