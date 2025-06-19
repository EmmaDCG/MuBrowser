package com.mu.executor;

import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public abstract class Executable {
   private int type;

   public Executable(int type) {
      this.type = type;
   }

   public int getType() {
      return this.type;
   }

   public abstract void execute(Game2GatewayPacket var1) throws Exception;

   public abstract void toPacket(ExecutePacket var1, Object... var2) throws Exception;
}
