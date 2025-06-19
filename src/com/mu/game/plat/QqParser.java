package com.mu.game.plat;

import com.mu.game.model.unit.player.Player;
import com.mu.io.http.servlet.plat.qq.compass.CompassManager;
import java.util.HashMap;

public class QqParser extends LoginParser {
   public boolean checkSign(HashMap paras) {
      String pf = (String)paras.get("pf");
      return true;
   }

   public LoginInfo getLoginInfo(HashMap paras) {
      String uid = (String)paras.get("openid");
      String sid = (String)paras.get("server_id");
      String openKey = (String)paras.get("openkey");
      String pf = (String)paras.get("pf");
      String pfKey = (String)paras.get("pfkey");
      String access_token = (String)paras.get("access_token");
      String os = (String)paras.get("serverid");
      LoginInfo info = new LoginInfo();
      info.setServerID(Integer.parseInt(sid));
      if (os != null) {
         info.setOpenServerId(Integer.parseInt(os));
      }

      info.setUserName(uid);
      info.setOpenId(uid);
      info.setAccess_token(access_token);
      info.setPf(pf);
      info.setPfKey(pfKey);
      info.setOpenKey(openKey);
      return info;
   }

   public boolean needPostWhenCreate() {
      return true;
   }

   public void doCreateRole(Player player) {
      CompassManager.reportRegister(player);
   }

   public void doQuit(Player player) {
      CompassManager.reportQuit(player);
   }

   public void doLogin(Player player) {
      CompassManager.reportLogin(player);
   }

   public boolean needPostWhenLogin() {
      return true;
   }

   public void doConsume(Player player, int ingot) {
      CompassManager.reportConsume(player, ingot);
   }

   public void doRecharge(Player player, int ingot) {
      CompassManager.reportRecharge(player, ingot);
   }

   public void doOnlineNum(int num) {
      CompassManager.reportOnline(num);
   }
}
