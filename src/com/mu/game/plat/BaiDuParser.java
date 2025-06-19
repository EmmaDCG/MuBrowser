package com.mu.game.plat;

import com.mu.config.Global;
import com.mu.game.model.unit.player.ClubUser;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.MD5;
import com.mu.utils.Time;
import java.net.URLDecoder;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaiDuParser extends LoginParser {
   private static Logger logger = LoggerFactory.getLogger(BaiDuParser.class);

   public boolean checkSign(HashMap paras) {
      try {
         String uid = (String)paras.get("user_id");
         String sid = (String)paras.get("server_id");
         String cm = ((String)paras.get("cm_flag")).toLowerCase();
         String time = (String)paras.get("timestamp");
         String sign = (String)paras.get("sign");
         String apiKey = (String)paras.get("api_key");
         String certKey = Global.getKey("cert");
         uid = URLDecoder.decode(uid, "utf-8");
         String tmpSign = certKey + "api_key" + apiKey + "cm_flag" + cm + "server_id" + sid + "timestamp" + time + "user_id" + uid;
         if (MD5.md5s(tmpSign).toLowerCase().equals(sign.toLowerCase())) {
            return true;
         } else {
            logger.error("cehck sign false");
            logger.error("cr_key = {}", Global.getLoginKey());
            return false;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
         return false;
      }
   }

   public boolean needPostWhenCreate() {
      return false;
   }

   public boolean needPostWhenLevelUp(Player player) {
      return false;
   }

   public LoginInfo getLoginInfo(HashMap paras) {
      String uid = (String)paras.get("user_id");
      String sid = (String)paras.get("server_id");
      String cmFlag = (String)paras.get("cm_flag");
      String memberInfo = (String)paras.get("memberInfo");
      String userSign = (String)paras.get("userSign");
      LoginInfo info = new LoginInfo();
      info.setNeedAntiAddiction(!cmFlag.equals("n"));
      info.setServerID(Integer.parseInt(sid));
      info.setUserName(uid);
      if (memberInfo != null && userSign != null && MD5.md5s(Global.getKey("cert") + memberInfo).equals(userSign.toLowerCase())) {
         try {
            String[] s = memberInfo.split("#_#");
            ClubUser cu = new ClubUser();
            int vipLevel = Integer.parseInt(s[0]);
            int memberLevel = Integer.parseInt(s[1]);
            boolean isAnnualMember = s[2].toLowerCase().equals("y");
            boolean isMember = s[3].toLowerCase().equals("y");
            boolean isVip = s[4].toLowerCase().equals("y");
            String userName = s[5];
            String expireTime = s[6];
            if (userName.contains(uid)) {
               long eTime = Time.getTimeStringToMills(expireTime, "yyyy-MM-dd HH:mm:ss");
               if (eTime > System.currentTimeMillis()) {
                  if (isVip) {
                     cu.setVipLevel(vipLevel);
                  }

                  if (isMember) {
                     cu.setMemberLevel(memberLevel);
                  }

                  cu.setAnnualMember(isAnnualMember);
                  cu.setMemberExpireTime(eTime);
               }
            }
         } catch (Exception var19) {
            var19.printStackTrace();
         }
      }

      return info;
   }
}
