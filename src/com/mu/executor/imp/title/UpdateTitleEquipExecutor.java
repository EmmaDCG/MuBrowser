package com.mu.executor.imp.title;

import com.mu.db.manager.TitleDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class UpdateTitleEquipExecutor extends Executable {
   public UpdateTitleEquipExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long rid = packet.readLong();
      int tid = packet.readInt();
      boolean isEquip = packet.readBoolean();
      TitleDBManager.updateEquip(rid, tid, isEquip);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      int tid = ((Integer)obj[1]).intValue();
      boolean isEquip = ((Boolean)obj[2]).booleanValue();
      packet.writeLong(rid);
      packet.writeInt(tid);
      packet.writeBoolean(isEquip);
   }
}
