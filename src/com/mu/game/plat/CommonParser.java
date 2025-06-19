package com.mu.game.plat;

import com.mu.config.Global;
import com.mu.utils.MD5;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonParser extends LoginParser {
   private static Logger logger = LoggerFactory.getLogger(CommonParser.class);

   public boolean checkSign(HashMap paras) {
      try {
         String uid = (String)paras.get("user_name");
         String sid = (String)paras.get("server_id");
         String is_adult = ((String)paras.get("is_adult")).toLowerCase();
         String time = (String)paras.get("time");
         String sign = (String)paras.get("sign");
         if (MD5.md5s(uid + sid + is_adult + time + Global.getLoginKey()).toLowerCase().equals(sign)) {
            return true;
         } else {
            logger.error("cehck sign false");
            logger.error("cr_key = {}", Global.getLoginKey());
            return false;
         }
      } catch (Exception var7) {
         var7.printStackTrace();
         return false;
      }
   }

   public LoginInfo getLoginInfo(HashMap paras) {
      String uid = (String)paras.get("user_name");
      String sid = (String)paras.get("server_id");
      String cmFlag = (String)paras.get("is_adult");
      String gd = (String)paras.get("gd");
      String isClient = (String)paras.get("is_client");
      LoginInfo info = new LoginInfo();
      info.setNeedAntiAddiction(!cmFlag.equals("1"));
      info.setServerID(Integer.parseInt(sid));
      info.setUserName(uid);
      info.setClient(isClient != null && isClient.equals("2"));
      info.setGuider(this.isGuider(gd, uid));
      return info;
   }
}
