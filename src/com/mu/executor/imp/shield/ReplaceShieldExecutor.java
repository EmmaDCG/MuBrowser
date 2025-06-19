package com.mu.executor.imp.shield;

import com.mu.db.manager.ShieldDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ReplaceShieldExecutor extends Executable {
   public ReplaceShieldExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int level = packet.readInt();
      int rank = packet.readInt();
      int star = packet.readInt();
      int rankExp = packet.readInt();
      ShieldDBManager.replaceShield(roleId, level, rank, star, rankExp);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      try {
         long roleId = ((Long)obj[0]).longValue();
         int level = ((Integer)obj[1]).intValue();
         int rank = ((Integer)obj[2]).intValue();
         int star = ((Integer)obj[3]).intValue();
         int rankExp = ((Integer)obj[4]).intValue();
         packet.writeLong(roleId);
         packet.writeInt(level);
         packet.writeInt(rank);
         packet.writeInt(star);
         packet.writeInt(rankExp);
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }
}
