package com.mu.executor.imp.extarget;

import com.mu.db.manager.ExtargetDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ExtargetReceiveExecutor extends Executable {
   public ExtargetReceiveExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      ExtargetDBManager.saveReceive(packet.readLong(), packet.readInt());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
      packet.writeInt(((Integer)obj[1]).intValue());
   }
}
