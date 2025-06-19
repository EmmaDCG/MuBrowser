package com.mu.game.plat;

import com.mu.game.model.unit.player.Player;
import com.mu.utils.Tools;
import java.net.URLDecoder;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoginParser {
   public static final int Parser_Common = 1;
   public static final int Parser_BaiDu = 2;
   public static final int Parser_G2 = 3;
   public static final int Parser_Ku7 = 4;
   public static final int Parser_Qq = 5;
   public static final int Parser_Small = 6;
   private static final Logger logger = LoggerFactory.getLogger(LoginParser.class);

   public abstract boolean checkSign(HashMap var1);

   public abstract LoginInfo getLoginInfo(HashMap var1);

   public void doNewRegister(long rid) {
   }

   public void doNewPlayer(Player player) {
   }

   public void doQuit(Player player) {
   }

   public void doConsume(Player player, int ingot) {
   }

   public void doOnlineNum(int num) {
   }

   public void doRecharge(Player player, int ingot) {
   }

   public HashMap getAllParameters(String tag) {
      int index = tag.indexOf("?");
      if (index != -1) {
         tag = tag.substring(index + 1);
      }

      HashMap map = new HashMap();

      try {
         String[] s = tag.split("&");

         for(int i = 0; i < s.length; ++i) {
            int index1 = s[i].indexOf("=");
            String key = s[i].substring(0, index1);
            String value = s[i].substring(index1 + 1);
            map.put(key, URLDecoder.decode(value, "utf-8"));
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      return map;
   }

   public void doCreateRole(Player player) {
   }

   public void doRoleLevelUp(Player player) {
   }

   public boolean needPostWhenCreate() {
      return false;
   }

   public boolean needPostWhenLevelUp(Player player) {
      return false;
   }

   public boolean needPostWhenLogin() {
      return false;
   }

   public void doLogin(Player player) {
   }

   public String getServerName(Player player) {
      return String.valueOf(player.getUser().getServerID());
   }

   public boolean isGuider(String gd, String uid) {
      return gd != null && gd.trim().equals("1") ? Tools.isGuilder(uid) : false;
   }
}
