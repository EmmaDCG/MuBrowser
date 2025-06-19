package com.mu.io.game.packet.imp.account;

import com.mu.config.Global;
import com.mu.config.MessageText;
import com.mu.db.manager.GlobalLogDBManager;
import com.mu.db.manager.UserDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.User;
import com.mu.game.plat.LoginInfo;
import com.mu.game.plat.LoginUtil;
import com.mu.io.game.packet.imp.player.UserSystemSetup;
import com.mu.io.game.packet.imp.sys.DisconnectReason;
import com.mu.io.http.servlet.plat.qq.api.APIManager;
import com.mu.utils.Time;
import org.jboss.netty.channel.Channel;

public class GetRoleListHandler implements Runnable {
   private Channel channel;
   private LoginInfo info = null;

   public GetRoleListHandler(LoginInfo info, Channel channel) {
      this.info = info;
      this.channel = channel;
   }

   public void run() {
      User user = UserDBManager.getUser(UserDBManager.registerUser(this.info.getUserName(), this.info.getServerID(), this.info.isNeedAntiAddiction()));
      if (user != null) {
         user.setRemoteIp(CenterManager.getIpByChannel(this.channel));
         if (Global.getParserID() == 5) {
            user.setAccess_token(this.info.getAccess_token());
            user.setOpenId(this.info.getOpenId());
            user.setPf(this.info.getPf());
            user.setOpenKey(this.info.getOpenKey());
            user.setPfKey(this.info.getPfKey());
            user.setOpenServerId(this.info.getOpenServerId());

            try {
               user.getBlueVip().init(APIManager.getBlueVipInfo(user));
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }

         GlobalLogDBManager.registerUser(user.getName(), user.getServerID(), Time.getTimeStr(), user.getBlueVip().getTag(), user.getRemoteIp());
         UserSystemSetup.pushSetup(user, this.channel);
         this.info.setUser(user);
         Player alreadyLoginPlayer = CenterManager.getPlayerByUserName(user.getName(), user.getServerID());
         Channel tmpChannel;
         if (alreadyLoginPlayer != null) {
            if (!alreadyLoginPlayer.isDestroy()) {
               tmpChannel = alreadyLoginPlayer.getChannel();
               this.info.setNewChannel(this.channel);
               this.info.setOldChannel(tmpChannel);
               CenterManager.logOutByRelogin(alreadyLoginPlayer, this.info);
               if (this.channel != null) {
                  this.channel.close();
               }

               CenterManager.removePlayer(alreadyLoginPlayer.getID(), alreadyLoginPlayer.getName(), user.getName(), user.getServerID(), alreadyLoginPlayer.getChannel());

               try {
                  Map map = alreadyLoginPlayer.getMap();
                  if (map != null) {
                     map.doRemovePlayerForDestroy(alreadyLoginPlayer);
                  }

                  alreadyLoginPlayer.setDestroy(true);
               } catch (Exception var5) {
                  var5.printStackTrace();
               }

               user.destroy();
               this.info.destroy();
               return;
            }

            CenterManager.removePlayer(alreadyLoginPlayer.getID(), alreadyLoginPlayer.getName(), user.getName(), user.getServerID(), alreadyLoginPlayer.getChannel());
         } else {
            tmpChannel = CenterManager.getInterChannel(user.getName(), user.getServerID());
            if (tmpChannel != null && tmpChannel.isWritable()) {
               this.info.setNewChannel(this.channel);
               DisconnectReason.pushDisconnect(this.channel, MessageText.getText(11));
               CenterManager.logOutByInterRelogin(user.getName(), user.getServerID(), this.info, tmpChannel);
               if (this.channel != null) {
                  this.channel.close();
               }

               user.destroy();
               this.info.destroy();
            } else {
               Player player = LoginUtil.loginGame(this.info, this.channel, false);
               GetRoleList.sendRoles(player, this.channel);
            }
         }

      }
   }
}
