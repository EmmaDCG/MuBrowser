package com.mu.executor.imp.title;

import com.mu.db.manager.TitleDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class InsertTitleExecutor extends Executable {
   public InsertTitleExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long rid = packet.readLong();
      int tid = packet.readInt();
      long time = packet.readLong();
      boolean isEquip = packet.readBoolean();
      TitleDBManager.insertTitle(rid, tid, time, isEquip);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      int tid = ((Integer)obj[1]).intValue();
      long time = ((Long)obj[2]).longValue();
      boolean isEquip = ((Boolean)obj[3]).booleanValue();
      packet.writeLong(rid);
      packet.writeInt(tid);
      packet.writeLong(time);
      packet.writeBoolean(isEquip);
   }
}
