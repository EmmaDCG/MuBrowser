package com.mu.game.plat;

import com.mu.config.Global;
import com.mu.utils.MD5;
import java.net.URLDecoder;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ku7Parser extends LoginParser {
   private static Logger logger = LoggerFactory.getLogger(Ku7Parser.class);

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

   public LoginInfo getLoginInfo(HashMap paras) {
      String uid = (String)paras.get("user_id");
      String sid = (String)paras.get("server_id");
      String cmFlag = (String)paras.get("cm_flag");
      LoginInfo info = new LoginInfo();
      info.setNeedAntiAddiction(!cmFlag.equals("n"));
      info.setServerID(Integer.parseInt(sid));
      info.setUserName(uid);
      return info;
   }
}
