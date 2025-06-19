package com.mu.executor.imp.financing;

import com.mu.db.manager.FinancingDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ReplaceFinancingRewardExecutor extends Executable {
   public ReplaceFinancingRewardExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int itemId = packet.readByte();
      long receiveTime = packet.readLong();
      FinancingDBManager.replaceReward(roleId, itemId, receiveTime);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      try {
         packet.writeLong(((Long)obj[0]).longValue());
         packet.writeByte(((Integer)obj[1]).intValue());
         packet.writeLong(((Long)obj[2]).longValue());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
