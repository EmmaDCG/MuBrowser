package com.mu.game.plat;

import com.mu.utils.MD5;
import java.net.URLDecoder;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmallParser extends LoginParser {
   private static Logger logger = LoggerFactory.getLogger(SmallParser.class);
   private static final String key = "e47b3de8b37d81140ac57f3dd93172e2";

   public boolean checkSign(HashMap paras) {
      try {
         String uid = (String)paras.get("user_name");
         String sid = (String)paras.get("server_id");
         String time = (String)paras.get("time");
         String sign = (String)paras.get("sign");
         uid = URLDecoder.decode(uid, "utf-8");
         String tmpSign = "passport=" + uid + "&sid=" + sid + "&time=" + time + "&key=" + "e47b3de8b37d81140ac57f3dd93172e2";
         if (MD5.md5s(tmpSign).toLowerCase().equals(sign.toLowerCase())) {
            return true;
         } else {
            logger.error("cehck sign false");
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
      LoginInfo info = new LoginInfo();
      info.setServerID(Integer.parseInt(sid));
      info.setUserName(uid);
      return info;
   }
}
