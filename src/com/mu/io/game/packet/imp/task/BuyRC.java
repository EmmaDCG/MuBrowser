package com.mu.io.game.packet.imp.task;

import com.mu.io.game.packet.ReadAndWritePacket;

public class BuyRC extends ReadAndWritePacket {
   public BuyRC(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.getPlayer().getTaskManager().rcBuy();
   }
}
