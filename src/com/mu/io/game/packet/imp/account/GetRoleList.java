package com.mu.io.game.packet.imp.account;

import com.mu.config.Global;
import com.mu.db.manager.PlayerDBManager;
import com.mu.db.manager.UserDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.Profession;
import com.mu.game.model.unit.player.RoleInfo;
import com.mu.game.plat.LoginInfo;
import com.mu.game.plat.LoginParser;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.sys.AdvanceClientConfig;
import com.mu.io.game.packet.imp.sys.BottomMessage;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.util.ArrayList;
import java.util.HashMap;
import org.jboss.netty.channel.Channel;

public class GetRoleList extends ReadAndWritePacket {
   private static AdvanceClientConfig ac = AdvanceClientConfig.getConfig();

   public GetRoleList(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public GetRoleList() {
      super(10000, (byte[])null);
   }

   public void process() throws Exception {
      if (!Global.isInterServiceServer()) {
         int type = this.readByte();
         String tag = this.readUTF();
         if (Global.isCheckPwd()) {
            String pwd = this.readUTF().trim();
            if (pwd.equals("") || !PlayerDBManager.checkPwd(tag, pwd)) {
               SystemMessage.writeMessage(this.getChannel(), "账号密码错误,请重新输入");
               return;
            }
         }

         if (type == 0) {
            if (!Global.isPwLogin()) {
               return;
            }

            if (Tools.isChineseChar(tag)) {
               SystemMessage.writeMessage(this.getChannel(), "账号不能包含中文");
               return;
            }

            this.doLoginByPwd(tag, Global.getServerID(), this.getChannel());
         } else {
            this.doLoginByUrl(tag, this.getChannel());
         }

      }
   }

   private void doLoginByPwd(String userName, int serverId, Channel channel) {
      boolean needAntiAddiction = UserDBManager.isFcm(userName, serverId);
      int sid = Global.getServerID();
      boolean isGuider = false;
      if (!Global.isBan(userName, CenterManager.getIpByChannel(channel))) {
         LoginInfo info = new LoginInfo();
         info.setNeedAntiAddiction(needAntiAddiction);
         info.setGuider(isGuider);
         info.setPlatServerName(String.valueOf(sid));
         info.setServerID(sid);
         info.setUserName(userName);
         ThreadCachedPoolManager.DB_SHORT.execute(new GetRoleListHandler(info, channel));
      }
   }

   private void doLoginByUrl(String paras, Channel channel) {
      LoginParser parser = Global.getLoginParser();
      HashMap map = parser.getAllParameters(paras);
      boolean isCorrect = parser.checkSign(map);
      if (isCorrect) {
         if (Global.isOpenWhiteList()) {
            String oid = (String)map.get("openid");
            if (oid == null || !Global.inWhiteList(oid)) {
               System.out.println("您不在测试白名单中,无法登陆");
               SystemMessage.writeMessage(channel, "您不在测试白名单中,无法登陆");
               BottomMessage.pushMessage(channel, "您不在测试白名单中,无法登陆");
               System.out.println("oid = " + oid);
               return;
            }
         }

         LoginInfo info = parser.getLoginInfo(map);
         if (Global.isBan(info.getUserName(), CenterManager.getIpByChannel(channel))) {
            return;
         }

         ThreadCachedPoolManager.DB_SHORT.execute(new GetRoleListHandler(info, channel));
      } else {
         writeLoginError(channel);
         System.out.println("is not Correct");
      }

      map.clear();
   }

   private static void writeLoginError(Channel channel) {
      try {
         GetRoleList gr = new GetRoleList();
         gr.writeBoolean(false);
         channel.write(gr.toBuffer());
         gr.destroy();
         gr = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void sendRoles(Player player, Channel channel) {
      player.writePacket(ac);

      try {
         GetRoleList grl = new GetRoleList();
         ArrayList roleList = player.getRoleList();
         int num = roleList.size();
         grl.writeBoolean(true);
         grl.writeByte(2);
         grl.writeByte(num);
         long now = System.currentTimeMillis();

         for(int i = 0; i < num; ++i) {
            RoleInfo roleInfo = (RoleInfo)roleList.get(i);
            writeRoleDetail(grl, roleInfo, now, player);
         }

         channel.write(grl.toBuffer());
         roleList = null;
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public static void writeRoleDetail(WriteOnlyPacket packet, RoleInfo roleInfo, long now, Player player) {
      try {
         packet.writeDouble((double)roleInfo.getID());
         packet.writeUTF(roleInfo.getName());
         packet.writeShort(roleInfo.getLevel());
         int pid = Profession.getProID(roleInfo.getProType(), roleInfo.getProLevel());
         packet.writeByte(pid);
         packet.writeShort(Profession.getProfession(pid).getLoginImg());
         MapData md = MapConfig.getMapData(roleInfo.getMapID());
         if (md.getInterMapType() == 2) {
            roleInfo.setMapID(10001);
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
