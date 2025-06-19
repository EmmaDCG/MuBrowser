package com.mu.io.game.packet.imp.init;

import com.mu.io.game.packet.ReadAndWritePacket;

public class InitTrialReceiveLogs extends ReadAndWritePacket {
   public InitTrialReceiveLogs(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }
}
