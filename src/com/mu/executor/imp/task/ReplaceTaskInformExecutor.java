package com.mu.executor.imp.task;

import com.mu.db.manager.TaskDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ReplaceTaskInformExecutor extends Executable {
   public ReplaceTaskInformExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int curRCStar = packet.readInt();
      int curRCRemain = packet.readInt();
      int sumRCBuy = packet.readInt();
      TaskDBManager.replaceInform(roleId, curRCStar, curRCRemain, sumRCBuy);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
      packet.writeInt(((Integer)obj[1]).intValue());
      packet.writeInt(((Integer)obj[2]).intValue());
      packet.writeInt(((Integer)obj[3]).intValue());
   }
}
