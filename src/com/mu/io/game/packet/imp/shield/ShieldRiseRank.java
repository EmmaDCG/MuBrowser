package com.mu.io.game.packet.imp.shield;

import com.mu.io.game.packet.ReadAndWritePacket;

public class ShieldRiseRank extends ReadAndWritePacket {
   public ShieldRiseRank(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.getPlayer().getShieldManager().riseRank();
   }
}
