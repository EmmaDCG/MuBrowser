package com.mu.executor.imp.rewardhall;

import com.mu.db.manager.RewardHallDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.rewardhall.sign.SignRewardData;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.util.Calendar;

public class ReplaceSignExecutor extends Executable {
   public ReplaceSignExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int round = packet.readInt();
      long roundStart = packet.readLong();
      long signTime = packet.readLong();
      byte[] timeSet = new byte[packet.readShort()];
      packet.readBytes(timeSet);
      byte[] rewardSet = new byte[packet.readByte()];
      packet.readBytes(rewardSet);
      RewardHallDBManager.replaceSign(roleId, timeSet, round, roundStart, signTime, rewardSet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
      packet.writeInt(((Integer)obj[1]).intValue());
      packet.writeLong(((Calendar)obj[2]).getTimeInMillis());
      packet.writeLong(((Calendar)obj[3]).getTimeInMillis());
      Calendar[] timeSet = (Calendar[])obj[4];
      packet.writeShort(timeSet.length * 8);

      for(int i = 0; i < timeSet.length; ++i) {
         packet.writeLong(timeSet[i].getTimeInMillis());
      }

      timeSet = null;
      SignRewardData[] rewardArr = (SignRewardData[])obj[5];
      packet.writeByte(rewardArr.length);

      for(int i = 0; i < rewardArr.length; ++i) {
         packet.writeByte(rewardArr[i].getId());
      }

      rewardArr = null;
   }
}
