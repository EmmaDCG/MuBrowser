package com.mu.executor.imp.extarget;

import com.mu.db.manager.ExtargetDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ExtargetCollectExecutor extends Executable {
   public ExtargetCollectExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      ExtargetDBManager.saveCollect(packet.readLong(), packet.readInt(), packet.readInt());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
      packet.writeInt(((Integer)obj[1]).intValue());
      packet.writeInt(((Integer)obj[2]).intValue());
   }
}
