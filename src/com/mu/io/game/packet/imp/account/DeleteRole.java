package com.mu.io.game.packet.imp.account;

import com.mu.io.game.packet.ReadAndWritePacket;

public class DeleteRole extends ReadAndWritePacket {
   public DeleteRole(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public void process() throws Exception {
   }
}
