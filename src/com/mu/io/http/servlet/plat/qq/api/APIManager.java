package com.mu.io.http.servlet.plat.qq.api;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.User;
import com.mu.game.qq.pay.QqPayElement;
import com.mu.game.qq.pay.Qqpay;
import com.mu.game.task.schedule.PayConfirmTask;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import flexjson.JSONDeserializer;
import java.util.HashMap;

public class APIManager {
   public static String blueVipActivityID = "UM160115163948690";
   public static final String sendNotificationName = "/v3/message/send_notification";
   public static final String getMultiInfoName = "/v3/user/get_multi_info";
   public static final String getFigureName = "/v3/user/get_figure";
   public static final String getBlueVipInfoName = "/v3/user/blue_vip_info";
   public static final String getAntiaddictionInfoName = "/v3/user/get_antiaddiction_info";
   public static final String getPayTokenName = "/v3/pay/get_token";
   public static final String buyGoodsName = "/v3/pay/buy_goods";
   public static final String confirmDeliveryName = "/v3/pay/confirm_delivery";

   public static void sendNotification(User user) {
      HashMap params = new HashMap();
      params.put("title", "hahahh");
      params.put("text", "heihei");
      params.put("imgurl", "http://minigameimg.qq.com/hello.gif");
      params.put("ctxcmd", "http://qq.com");
      params.put("viewcmd", "http://qq.com");
      params.put("charset", "utf-8");
      QQApi.callApi(user, "/v3/message/send_notification", params);
   }

   public static void getMultiInfo(User user) {
      HashMap params = new HashMap();
      params.put("ids", user.getOpenId());
      QQApi.callApi(user, "/v3/user/get_multi_info", params);
   }

   public static void getFigure(User user) {
      HashMap params = new HashMap();
      params.put("flag", "3");
      QQApi.callApi(user, "/v3/user/get_figure", params);
   }

   public static String getBlueVipInfo(User user) {
      HashMap params = new HashMap();
      return QQApi.callApi(user, "/v3/user/blue_vip_info", params);
   }

   public static void getAntiaddictionInfo(final Player player, final boolean first) {
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            HashMap params = new HashMap();
            int cmdType = 1;
            if (!first) {
               cmdType = 2;
            }

            params.put("cmd_type", String.valueOf(cmdType));
            User user = player.getUser();
            String json = QQApi.callApi(user, "/v3/user/get_antiaddiction_info", params);
            JSONDeserializer der = new JSONDeserializer();
            HashMap map = (HashMap)der.deserialize(json);
            int ret = ((Integer)map.get("ret")).intValue();
            if (ret == 0) {
               int audit = ((Integer)map.get("audit")).intValue();
               int gametime = ((Integer)map.get("gametime")).intValue();
               if (audit != 1 && !player.isDestroy() && gametime < 18000) {
                  ;
               }
            }

         }
      });
   }

   private static String getTs() {
      return "" + System.currentTimeMillis() / 1000L;
   }

   public static String getPayToken(Player player, int tokentype) {
      HashMap params = new HashMap();
      User user = player.getUser();
      params.put("pfkey", user.getPfKey());
      params.put("discountid", "1");
      params.put("ts", getTs());
      params.put("zoneid", String.valueOf(player.getUser().getServerID()));
      params.put("version", "v3");
      String json = QQApi.callApi(user, "/v3/pay/get_token", params);
      JSONDeserializer der = new JSONDeserializer();
      HashMap map = (HashMap)der.deserialize(json);
      System.out.println("开通包月" + json);
      int ret = ((Integer)map.get("ret")).intValue();
      if (ret == 0) {
         String token = (String)map.get("token");
         return token;
      } else {
         return null;
      }
   }

   public static String getBuyGoods(Player player, QqPayElement pe) {
      HashMap params = new HashMap();
      User user = player.getUser();
      params.put("pfkey", user.getPfKey());
      params.put("ts", getTs());
      params.put("payitem", pe.getPayItem());
      params.put("goodsmeta", pe.getDes());
      params.put("goodsurl", Qqpay.getImgUrl());
      params.put("zoneid", String.valueOf(player.getUser().getServerID()));
      String json = QQApi.callApi(user, "/v3/pay/buy_goods", params, "https");
      JSONDeserializer der = new JSONDeserializer();
      HashMap map = (HashMap)der.deserialize(json);
      int ret = ((Integer)map.get("ret")).intValue();
      if (ret == 0) {
         String urlParams = (String)map.get("url_params");
         return urlParams;
      } else {
         return null;
      }
   }

   public static void confirmDelivery(String openID, String openKey, String ts, String payitem, String token, String billno, String zoneid, String provide_errno, String amt, String payamt_coins, String sig) {
      HashMap params = new HashMap();
      params.put("ts", "" + System.currentTimeMillis() / 1000L);
      params.put("payitem", payitem);
      params.put("token_id", token);
      params.put("billno", billno);
      params.put("zoneid", zoneid);
      params.put("provide_errno", provide_errno);
      params.put("amt", amt);
      params.put("payamt_coins", payamt_coins);
      String json = QQApi.callApi(openID, openKey, "/v3/pay/confirm_delivery", params, "https");
      JSONDeserializer der = new JSONDeserializer();
      HashMap map = (HashMap)der.deserialize(json);
      int ret = ((Integer)map.get("ret")).intValue();
      PayConfirmTask.doByResult(ret, openID, billno);
      System.out.println("支付确认" + json);
   }
}
