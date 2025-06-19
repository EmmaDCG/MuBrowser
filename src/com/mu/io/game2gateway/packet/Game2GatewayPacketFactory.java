package com.mu.io.game2gateway.packet;

import com.mu.io.game2gateway.packet.imp.GS_DefaultPacket;
import com.mu.io.game2gateway.packet.imp.GS_ReadListPacket;
import com.mu.io.game2gateway.packet.imp.sys.GS_ExecutePacket;
import com.mu.io.game2gateway.packet.imp.sys.GS_NotifySwitchServer;

public class Game2GatewayPacketFactory {
   public static Game2GatewayPacket getPacket(int code, byte[] bytes) {
      switch(code) {
      case 103:
         return new GS_ExecutePacket(code, bytes);
      case 104:
         return new GS_NotifySwitchServer(code, bytes);
      case 60001:
         return new GS_ReadListPacket(code, bytes);
      default:
         return new GS_DefaultPacket(code, bytes);
      }
   }
}
