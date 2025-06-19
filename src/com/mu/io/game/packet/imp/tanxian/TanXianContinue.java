package com.mu.io.game.packet.imp.tanxian;

import com.mu.io.game.packet.ReadAndWritePacket;

public class TanXianContinue extends ReadAndWritePacket {
   public TanXianContinue(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      TanXianStart.sendMessageResult(this.getPlayer());
   }
}
