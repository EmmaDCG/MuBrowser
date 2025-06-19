package com.mu.io.game.packet.imp.sys;

import com.mu.db.manager.PlayerDBManager;
import com.mu.db.manager.UserDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.RoleInfo;
import com.mu.game.model.unit.player.User;
import com.mu.game.plat.LoginInfo;
import com.mu.game.plat.LoginUtil;
import com.mu.io.game.packet.ReadAndWritePacket;
import org.jboss.netty.channel.Channel;

public class TransferPlayerBaseInfo extends ReadAndWritePacket {
   public TransferPlayerBaseInfo() {
      super(108, (byte[])null);
   }

   public TransferPlayerBaseInfo(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public void process() throws Exception {
      int initType = this.readByte();
      byte[] userBytes = new byte[this.readInt()];
      this.readBytes(userBytes);
      User user = UserDBManager.getUser(userBytes);
      byte[] roleBytes = new byte[this.readInt()];
      this.readBytes(roleBytes);
      RoleInfo roleInfo = PlayerDBManager.getRoleInfo(roleBytes);
      LoginInfo loginInfo = new LoginInfo();
      loginInfo.setUser(user);
      loginInfo.setNeedAntiAddiction(user.isNeedAntiAddiction());
      loginInfo.setNewChannel(this.getChannel());
      loginInfo.setServerID(user.getServerID());
      loginInfo.setUserName(user.getName());
      Player player = null;
      if (initType == 2) {
         player = LoginUtil.loginGame(loginInfo, this.getChannel(), true);
      } else {
         player = LoginUtil.loginGame(loginInfo, this.getChannel(), false);
      }

      LoginUtil.initRole(player, roleInfo, initType);
      if (initType == 3) {
         CenterManager.removeInterChannel(user.getName(), user.getServerID());
      }

      player.setInitType(initType);
      userBytes = null;
   }

   public static void writeBaseInfo(Channel channel, long rid, String userName, boolean needAntiAddiction, int sid, int initType) {
      try {
         TransferPlayerBaseInfo ti = new TransferPlayerBaseInfo();
         ti.writeByte(initType);
         byte[] userBytes = UserDBManager.registerUser(userName, sid, needAntiAddiction);
         byte[] roleBytes = PlayerDBManager.getRoleInfoBytes(rid);
         ti.writeInt(userBytes.length);
         ti.writeBytes(userBytes);
         ti.writeInt(roleBytes.length);
         ti.writeBytes(roleBytes);
         userBytes = null;
         channel.write(ti.toBuffer());
         ti.destroy();
         ti = null;
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }
}
