package com.mu.executor.imp.sys;

import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import org.jboss.netty.channel.Channel;

public class CloseGameChannelExecutor extends Executable {
   public CloseGameChannelExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      Channel gsChannel = packet.getG2sChannel();
      if (gsChannel != null) {
         gsChannel.close();
      }

   }

   public void toPacket(ExecutePacket packet, Object... obj) {
   }
}
