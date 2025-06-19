package com.mu.executor.imp.player;

import com.mu.db.manager.GlobalLogDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveLogInOutExeCutor extends Executable {
   public SaveLogInOutExeCutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      GlobalLogDBManager.saveLogInOutLog(packet.readUTF(), packet.readLong(), packet.readInt(), packet.readUTF(), packet.readByte(), packet.readInt(), packet.readUTF());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      int type = ((Integer)obj[1]).intValue();
      packet.writeUTF(player.getUserName());
      packet.writeLong(player.getID());
      packet.writeInt(player.getUser().getServerID());
      packet.writeUTF(player.getName());
      packet.writeByte(type);
      packet.writeInt(player.getLevel());
      String ip = player.getUser().getRemoteIp();
      packet.writeUTF(ip == null ? "" : ip);
   }
}
