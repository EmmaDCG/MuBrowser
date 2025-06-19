package com.mu.io.game.packet.imp.equip;

import com.mu.io.game.packet.ReadAndWritePacket;

public class RepairHorseDura extends ReadAndWritePacket {
   public RepairHorseDura(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }
}
