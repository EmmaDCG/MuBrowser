package com.mu.io.game2gateway.packet.imp.sys;

import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class GS_NotifySwitchServer extends Game2GatewayPacket {
   public GS_NotifySwitchServer(int code, byte[] bytes) {
      super(code, bytes);
   }

   public void process() throws Exception {
   }
}
