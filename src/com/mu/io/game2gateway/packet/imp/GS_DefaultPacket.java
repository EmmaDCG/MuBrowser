package com.mu.io.game2gateway.packet.imp;

import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class GS_DefaultPacket extends Game2GatewayPacket {
   public GS_DefaultPacket(int code, byte[] bytes) {
      super(code, bytes);
   }

   public void process() {
      if (this.getC2gChannel() != null) {
         this.getC2gChannel().write(this.toBuffer());
      }

   }
}
