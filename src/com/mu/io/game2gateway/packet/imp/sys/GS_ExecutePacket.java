package com.mu.io.game2gateway.packet.imp.sys;

import com.mu.executor.Executor;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class GS_ExecutePacket extends Game2GatewayPacket {
   public GS_ExecutePacket(int code, byte[] bytes) {
      super(code, bytes);
   }

   public void process() throws Exception {
      int type = this.readShort();
      Executor.execute(type, this);
   }
}
