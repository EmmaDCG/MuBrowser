package com.mu.io.game.packet.imp.rewardhall.sign;

import com.mu.io.game.packet.ReadAndWritePacket;

public class SignBefore extends ReadAndWritePacket {
   public SignBefore(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      long date = (long)this.readDouble();
      this.getPlayer().getSignManager().signBefore(date);
   }
}
