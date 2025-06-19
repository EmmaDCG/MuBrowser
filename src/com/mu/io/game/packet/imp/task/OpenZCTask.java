package com.mu.io.game.packet.imp.task;

import com.mu.io.game.packet.ReadAndWritePacket;

public class OpenZCTask extends ReadAndWritePacket {
   public OpenZCTask(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }
}
