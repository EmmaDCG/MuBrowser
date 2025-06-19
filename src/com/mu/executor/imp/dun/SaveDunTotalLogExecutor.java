package com.mu.executor.imp.dun;

import com.mu.db.manager.DungeonDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveDunTotalLogExecutor extends Executable {
   public SaveDunTotalLogExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      DungeonDBManager.saveTotalLog(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      int dunId = ((Integer)obj[1]).intValue();
      int sid = ((Integer)obj[2]).intValue();
      int times = ((Integer)obj[3]).intValue();
      packet.writeLong(rid);
      packet.writeByte(dunId);
      packet.writeShort(sid);
      packet.writeInt(times);
   }
}
