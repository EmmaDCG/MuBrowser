package com.mu.executor.imp.rewardhall;

import com.mu.db.manager.RewardHallDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.rewardhall.vitality.VitalityRewardData;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ReplaceVitalityExecutor extends Executable {
   public ReplaceVitalityExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int vitality = packet.readInt();
      byte[] rewardSet = new byte[packet.readByte()];
      packet.readBytes(rewardSet);
      RewardHallDBManager.replaceVitality(roleId, vitality, rewardSet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
      packet.writeInt(((Integer)obj[1]).intValue());
      VitalityRewardData[] rewardArr = (VitalityRewardData[])obj[2];
      packet.writeByte(rewardArr.length);

      for(int i = 0; i < rewardArr.length; ++i) {
         packet.writeByte(rewardArr[i].getId());
      }

      rewardArr = null;
   }
}
