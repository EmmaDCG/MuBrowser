package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class DeleteEnemyExecutor extends Executable {
   public DeleteEnemyExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PlayerDBManager.deleteEnemy(packet.readLong(), packet.readLong());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      long eid = ((Long)obj[1]).longValue();
      packet.writeLong(rid);
      packet.writeLong(eid);
   }
}
