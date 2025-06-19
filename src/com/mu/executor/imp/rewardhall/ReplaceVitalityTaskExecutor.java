package com.mu.executor.imp.rewardhall;

import com.mu.db.manager.RewardHallDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ReplaceVitalityTaskExecutor extends Executable {
   public ReplaceVitalityTaskExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int taskId = packet.readInt();
      int rate = packet.readInt();
      RewardHallDBManager.replaceVitalityTask(roleId, taskId, rate);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
      packet.writeInt(((Integer)obj[1]).intValue());
      packet.writeInt(((Integer)obj[2]).intValue());
   }
}
