package com.mu.executor.imp.rewardhall;

import com.mu.db.manager.RewardHallDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ReplaceOnlineExecutor extends Executable {
   public ReplaceOnlineExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      String dayReceiveStr = packet.readUTF();
      long weekReceiveTime = packet.readLong();
      int weekIndex = packet.readInt();
      int weekSeconds = packet.readInt();
      int weekAccumulative = packet.readInt();
      RewardHallDBManager.replaceOnline(roleId, dayReceiveStr, weekReceiveTime, weekIndex, weekSeconds, weekAccumulative);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
      packet.writeUTF((String)obj[1]);
      packet.writeLong(((Long)obj[2]).longValue());
      packet.writeInt(((Integer)obj[3]).intValue());
      packet.writeInt(((Integer)obj[4]).intValue());
      packet.writeInt(((Integer)obj[5]).intValue());
   }
}
