package com.mu.io.game.packet.imp.account;

import com.mu.io.game.packet.ReadAndWritePacket;

public class RequestCreateRoleInfo extends ReadAndWritePacket {
   public RequestCreateRoleInfo(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public void process() throws Exception {
   }
}
