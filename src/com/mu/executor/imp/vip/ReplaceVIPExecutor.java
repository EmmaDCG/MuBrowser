package com.mu.executor.imp.vip;

import com.mu.db.manager.VIPDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ReplaceVIPExecutor extends Executable {
   public ReplaceVIPExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int vipId = packet.readByte();
      long activeTime = packet.readLong();
      int buyDays = packet.readShort();
      int expDays = packet.readShort();
      VIPDBManager.replaceVIP(roleId, vipId, activeTime, buyDays, expDays);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      try {
         packet.writeLong(((Long)obj[0]).longValue());
         packet.writeByte(((Integer)obj[1]).intValue());
         packet.writeLong(((Long)obj[2]).longValue());
         packet.writeShort(((Integer)obj[3]).intValue());
         packet.writeShort(((Integer)obj[4]).intValue());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
