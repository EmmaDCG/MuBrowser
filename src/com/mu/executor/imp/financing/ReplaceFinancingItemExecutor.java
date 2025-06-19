package com.mu.executor.imp.financing;

import com.mu.db.manager.FinancingDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ReplaceFinancingItemExecutor extends Executable {
   public ReplaceFinancingItemExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int itemId = packet.readByte();
      int loginDay = packet.readByte();
      FinancingDBManager.replaceItem(roleId, itemId, loginDay);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      try {
         packet.writeLong(((Long)obj[0]).longValue());
         packet.writeByte(((Integer)obj[1]).intValue());
         packet.writeByte(((Integer)obj[2]).intValue());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
