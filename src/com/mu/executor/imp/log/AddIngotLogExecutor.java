package com.mu.executor.imp.log;

import com.mu.config.Global;
import com.mu.db.log.LogDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class AddIngotLogExecutor extends Executable {
   public AddIngotLogExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      String userName = packet.readUTF();
      long roleID = packet.readLong();
      String roleName = packet.readUTF();
      int ingot = packet.readInt();
      int addType = packet.readShort();
      int roleServerId = packet.readInt();
      int serverID = packet.readInt();
      LogDBManager.addIngotLog(userName, roleID, roleName, ingot, addType, roleServerId, serverID);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      String userName = player.getUserName();
      long roleID = player.getID();
      String roleName = player.getName();
      int ingot = ((Integer)obj[1]).intValue();
      int type = ((Integer)obj[2]).intValue();
      packet.writeUTF(userName);
      packet.writeLong(roleID);
      packet.writeUTF(roleName);
      packet.writeInt(ingot);
      packet.writeShort(type);
      packet.writeInt(player.getUser().getServerID());
      packet.writeInt(Global.getServerID());
   }
}
