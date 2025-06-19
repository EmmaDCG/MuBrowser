package com.mu.io.game.packet.imp.gang;

import com.mu.io.game.packet.ReadAndWritePacket;

public class GangSendPacket extends ReadAndWritePacket {
   public GangSendPacket(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }
}
