package com.mu.executor.imp.activity;

import com.mu.db.manager.ActivityDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveRoleActivityReceiveExecutor extends Executable {
   public SaveRoleActivityReceiveExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long rid = packet.readLong();
      int eid = packet.readInt();
      String date = packet.readUTF();
      ActivityDBManager.insertRoleReceiveLogs(rid, eid, date);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      int eid = ((Integer)obj[1]).intValue();
      String date = (String)obj[2];
      packet.writeLong(rid);
      packet.writeInt(eid);
      packet.writeUTF(date);
   }
}
