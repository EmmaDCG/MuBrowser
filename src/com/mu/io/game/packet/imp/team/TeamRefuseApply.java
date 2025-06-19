package com.mu.io.game.packet.imp.team;

import com.mu.io.game.packet.ReadAndWritePacket;

public class TeamRefuseApply extends ReadAndWritePacket {
   public TeamRefuseApply(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }
}
