package com.mu.executor.imp.title;

import com.mu.db.manager.TitleDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class DeleteTitleExecutor extends Executable {
   public DeleteTitleExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long rid = packet.readLong();
      int tid = packet.readInt();
      TitleDBManager.deleteTitle(rid, tid);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      int tid = ((Integer)obj[1]).intValue();
      packet.writeLong(rid);
      packet.writeInt(tid);
   }
}
