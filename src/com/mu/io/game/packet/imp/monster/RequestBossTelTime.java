package com.mu.io.game.packet.imp.monster;

import com.mu.io.game.packet.ReadAndWritePacket;

public class RequestBossTelTime extends ReadAndWritePacket {
   public RequestBossTelTime(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }
}
