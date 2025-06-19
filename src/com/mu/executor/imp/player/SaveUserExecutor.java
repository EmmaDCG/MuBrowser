package com.mu.executor.imp.player;

import com.mu.db.manager.UserDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.User;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveUserExecutor extends Executable {
   public SaveUserExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      UserDBManager.saveUser(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      User user = (User)obj[0];
      packet.writeUTF(user.getName());
      packet.writeLong(user.getLastLoginTime());
      packet.writeLong(user.getLastLogoutTime());
      packet.writeInt(user.getOnlineTime());
      packet.writeInt(user.getRestTime());
      packet.writeInt(user.getServerID());
      packet.writeUTF(user.getBlueVip().getTag());
      byte[] setBytes = user.getSetupBytes();
      packet.writeInt(setBytes.length);
      packet.writeBytes(setBytes);
   }
}
