package com.mu.io.game.packet.imp.financing;

import com.mu.io.game.packet.ReadAndWritePacket;

public class InitFinancing extends ReadAndWritePacket {
   public InitFinancing(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitFinancing() {
      super(42000, (byte[])null);
   }

   public void process() throws Exception {
      this.getPlayer().getFinancingManager().init(this);
   }
}
