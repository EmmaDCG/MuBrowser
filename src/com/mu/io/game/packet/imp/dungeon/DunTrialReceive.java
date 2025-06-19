package com.mu.io.game.packet.imp.dungeon;

import com.mu.io.game.packet.ReadAndWritePacket;

public class DunTrialReceive extends ReadAndWritePacket {
   public DunTrialReceive(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public DunTrialReceive() {
      super(12011, (byte[])null);
   }

   public void process() throws Exception {
   }
}
