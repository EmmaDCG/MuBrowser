package com.mu.executor.imp.sys;

import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class InterServerReLogin extends Executable {
   public InterServerReLogin(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      packet.getG2sChannel().close();
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      String userName = (String)obj[0];

      try {
         packet.writeUTF(userName);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
