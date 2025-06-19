package com.mu.executor.imp.rewardhall;

import com.mu.db.manager.RewardHallDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ClearVitalityTaskExecutor extends Executable {
   public ClearVitalityTaskExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      RewardHallDBManager.clearVitalityTask(roleId);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
   }
}
