package com.mu.executor.imp.tanxian;

import com.mu.db.manager.TanXianDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class UpdateTanXianExecutor extends Executable {
   public UpdateTanXianExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      TanXianDBManager.update(packet.readLong(), packet.readInt(), packet.readInt(), packet.readInt());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
      packet.writeInt(((Integer)obj[1]).intValue());
      packet.writeInt(((Integer)obj[2]).intValue());
      packet.writeInt(((Integer)obj[3]).intValue());
   }
}
