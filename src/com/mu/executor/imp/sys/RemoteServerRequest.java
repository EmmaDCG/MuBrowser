package com.mu.executor.imp.sys;

import com.mu.config.Global;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.io.game2gateway.server.GatewaySocketClient;

public class RemoteServerRequest extends Executable {
   public RemoteServerRequest(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      String host = packet.readUTF();
      int port = packet.readUnsignedShort();
      int initType = packet.readByte();
      GatewaySocketClient.connect(packet.getC2gChannel(), 1, host, port, initType);
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      boolean isToInterServer = ((Boolean)obj[0]).booleanValue();

      try {
         if (isToInterServer) {
            packet.writeUTF(Global.getInterServerHost());
            packet.writeShort(Global.getInterServerPort());
            packet.writeByte(2);
         } else {
            Player player = (Player)obj[1];
            packet.writeUTF("127.0.0.1");
            packet.writeShort(player.getRemotePort());
            packet.writeByte(3);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
