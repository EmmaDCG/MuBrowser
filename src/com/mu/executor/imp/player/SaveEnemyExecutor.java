package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveEnemyExecutor extends Executable {
   public SaveEnemyExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long rid = packet.readLong();
      long eid = packet.readLong();
      int times = packet.readUnsignedShort();
      PlayerDBManager.saveEnemy(rid, eid, times);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      long eid = ((Long)obj[1]).longValue();
      int times = ((Integer)obj[2]).intValue();
      packet.writeLong(rid);
      packet.writeLong(eid);
      packet.writeShort(times);
   }
}
