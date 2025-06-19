package com.mu.executor.imp.activity;

import com.mu.db.manager.ActivityDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveUserActivityReceiveExecutor extends Executable {
   public SaveUserActivityReceiveExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      String userName = packet.readUTF();
      int sid = packet.readInt();
      int eid = packet.readInt();
      String date = packet.readUTF();
      ActivityDBManager.insertUserReceiveLogs(userName, sid, eid, date);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      String userName = (String)obj[0];
      int sid = ((Integer)obj[1]).intValue();
      int eid = ((Integer)obj[2]).intValue();
      String date = (String)obj[3];
      packet.writeUTF(userName);
      packet.writeInt(sid);
      packet.writeInt(eid);
      packet.writeUTF(date);
   }
}
