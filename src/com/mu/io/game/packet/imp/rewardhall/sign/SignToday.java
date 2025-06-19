package com.mu.io.game.packet.imp.rewardhall.sign;

import com.mu.io.game.packet.ReadAndWritePacket;

public class SignToday extends ReadAndWritePacket {
   public SignToday(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.getPlayer().getSignManager().signToday();
   }
}
