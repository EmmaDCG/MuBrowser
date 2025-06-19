package com.mu.io.game.packet.imp.tanxian;

import com.mu.io.game.packet.ReadAndWritePacket;

public class TanXianInit extends ReadAndWritePacket {
   public TanXianInit(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.getPlayer().getTanXianManager().init(this);
   }
}
