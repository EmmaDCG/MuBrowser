package com.mu.io.game.packet.imp.equip;

import com.mu.io.game.packet.ReadAndWritePacket;

public class ConversionStone extends ReadAndWritePacket {
   public ConversionStone(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }
}
