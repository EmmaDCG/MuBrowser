package com.mu.io.game.packet.imp.shield;

import com.mu.io.game.packet.ReadAndWritePacket;

public class InitShield extends ReadAndWritePacket {
   public InitShield() {
      super(45201, (byte[])null);
   }

   public InitShield(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.getPlayer().getShieldManager().onLoginInit(this);
   }
}
