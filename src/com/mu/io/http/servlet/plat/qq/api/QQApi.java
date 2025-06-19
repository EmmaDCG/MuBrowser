package com.mu.io.http.servlet.plat.qq.api;

import com.mu.config.Global;
import com.mu.game.model.unit.player.User;
import com.qq.open.OpenApiV3;
import java.util.HashMap;

public class QQApi {
   private static OpenApiV3 sdk = null;

   static {
      sdk = new OpenApiV3(Global.appid, Global.appkey);
      sdk.setServerName(Global.QQAPIIp);
   }

   public static String callApi(String openId, String openKey, String scriptName, HashMap params, String protocol) {
      String json = "";

      try {
         params.put("openid", openId);
         params.put("openkey", openKey);
         params.put("pf", "qqgame");
         params.put("charset", "utf-8");
         json = sdk.api(scriptName, params, protocol);
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return json;
   }

   public static String callApi(User user, String scriptName, HashMap params) {
      return callApi(user, scriptName, params, "http");
   }

   public static String callApi(User user, String scriptName, HashMap params, String protocol) {
      return callApi(user.getOpenId(), user.getOpenKey(), scriptName, params, protocol);
   }
}
