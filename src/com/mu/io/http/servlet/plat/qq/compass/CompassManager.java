package com.mu.io.http.servlet.plat.qq.compass;

import com.mu.config.Global;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;

public class CompassManager {
   public static final String loginName = "/report_login.php";
   public static final String registerName = "/report_register.php";
   public static final String acceptName = "/report_accept.php";
   public static final String inviteName = "/report_invite.php";
   public static final String consumeName = "/report_consume.php";
   public static final String rechargeName = "/report_recharge.php";
   public static final String quitName = "/report_quit.php";
   public static final String onlineName = "/report_online.php";

   public static void reportOnline(final int userNum) {
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            ArrayList list = CompassManager.createSystemParams();
            list.add(new BasicNameValuePair("user_num", "" + userNum));
            CompassManager.callApi("/report_online.php", list);
            list.clear();
         }
      });
   }

   public static void reportQuit(final Player player) {
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            ArrayList list = CompassManager.analysisCommonParams(player);
            list.add(new BasicNameValuePair("onlinetime", "" + player.getThisOnlineTime()));
            list.add(new BasicNameValuePair("level", "" + player.getLevel()));
            CompassManager.callApi("/report_quit.php", list);
            list.clear();
         }
      });
   }

   public static void reportRecharge(final Player player, final int ingot) {
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            ArrayList list = CompassManager.analysisCommonParams(player);
            list.add(new BasicNameValuePair("modifyfee", "" + CompassManager.converseIngotToQMoney(ingot)));
            CompassManager.callApi("/report_recharge.php", list);
            list.clear();
         }
      });
   }

   public static void reportConsume(final Player player, final int ingot) {
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            ArrayList list = CompassManager.analysisCommonParams(player);
            int qMoney = CompassManager.converseIngotToQMoney(ingot);
            list.add(new BasicNameValuePair("modifyfee", "" + qMoney));
            list.add(new BasicNameValuePair("totalfee", "" + player.getIngot()));
            list.add(new BasicNameValuePair("level", "" + player.getLevel()));
            CompassManager.callApi("/report_consume.php", list);
            list.clear();
         }
      });
   }

   public static int converseIngotToQMoney(int ingot) {
      return ingot;
   }

   public static void reportInvite(final Player player) {
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            ArrayList list = CompassManager.analysisCommonParams(player);
            list.add(new BasicNameValuePair("touid", "110002042"));
            list.add(new BasicNameValuePair("toopenid", "1F07F4372FAB28EDEC2ABB3968EC5645"));
            CompassManager.callApi("/report_invite.php", list);
            list.clear();
         }
      });
   }

   public static void reportLogin(final Player player) {
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            ArrayList list = CompassManager.analysisCommonParams(player);
            list.add(new BasicNameValuePair("level", String.valueOf(player.getLevel())));
            CompassManager.callApi("/report_login.php", list);
            list.clear();
         }
      });
   }

   public static void reportRegister(final Player player) {
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            ArrayList list = CompassManager.analysisCommonParams(player);
            CompassManager.callApi("/report_register.php", list);
            list.clear();
         }
      });
   }

   public static void reportAccept(final Player player) {
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            ArrayList list = CompassManager.analysisCommonParams(player);
            CompassManager.callApi("/report_accept.php", list);
            list.clear();
         }
      });
   }

   private static ArrayList analysisCommonParams(Player player) {
      ArrayList params = new ArrayList();
      params.add(new BasicNameValuePair("userip", "" + ipToLong(player.getUser().getRemoteIp())));
      setCommonParams(params);
      params.add(new BasicNameValuePair("opuid", String.valueOf(player.getID())));
      params.add(new BasicNameValuePair("opopenid", player.getUser().getOpenId()));
      return params;
   }

   private static ArrayList createSystemParams() {
      ArrayList params = new ArrayList();
      params.add(new BasicNameValuePair("userip", "" + ipToLong(Global.getServerIp())));
      setCommonParams(params);
      return params;
   }

   private static void setCommonParams(ArrayList params) {
      params.add(new BasicNameValuePair("version", "1"));
      params.add(new BasicNameValuePair("appid", Global.appid));
      params.add(new BasicNameValuePair("svrip", "" + ipToLong(Global.getServerIp())));
      params.add(new BasicNameValuePair("time", "" + System.currentTimeMillis() / 1000L));
      params.add(new BasicNameValuePair("domain", "10"));
      params.add(new BasicNameValuePair("worldid", "1"));
   }

   private static String callApi(String scriptName, ArrayList params) {
      String json = "";

      try {
         String[] result = Tools.getUrlPostContent("http://tencentlog.com/stat" + scriptName, params);
         json = result[1];
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return json;
   }

   public static long ipToLong(String ipAddress) {
      long result = 0L;
      String[] ipAddressInArray = ipAddress.split("\\.");

      for(int i = 3; i >= 0; --i) {
         long ip = Long.parseLong(ipAddressInArray[3 - i]);
         result |= ip << i * 8;
      }

      return result;
   }
}
