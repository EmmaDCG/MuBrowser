package com.mu.io.game.packet.imp.equip;

import com.mu.io.game.packet.ReadAndWritePacket;

public class RequestConversionStone extends ReadAndWritePacket {
   public RequestConversionStone(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() {
   }
}
