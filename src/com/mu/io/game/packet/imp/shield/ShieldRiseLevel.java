package com.mu.io.game.packet.imp.shield;

import com.mu.io.game.packet.ReadAndWritePacket;

public class ShieldRiseLevel extends ReadAndWritePacket {
   public ShieldRiseLevel(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      boolean result = this.getPlayer().getShieldManager().riseLevel();
      if (result) {
         this.writeBoolean(true);
         this.getPlayer().writePacket(this);
         ShieldInform.sendMsgShieldInform(this.getPlayer().getShieldManager());
      }

   }
}
