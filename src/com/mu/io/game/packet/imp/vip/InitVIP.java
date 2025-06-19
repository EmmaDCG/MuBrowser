package com.mu.io.game.packet.imp.vip;

import com.mu.io.game.packet.ReadAndWritePacket;

public class InitVIP extends ReadAndWritePacket {
   public InitVIP(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitVIP() {
      super(41000, (byte[])null);
   }

   public void process() throws Exception {
      this.getPlayer().getVIPManager().init(this);
   }
}
