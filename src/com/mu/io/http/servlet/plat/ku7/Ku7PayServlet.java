package com.mu.io.http.servlet.plat.ku7;

import com.mu.config.Global;
import com.mu.db.log.IngotChangeType;
import com.mu.db.log.LogDBManager;
import com.mu.db.manager.PayDBManager;
import com.mu.db.manager.PlayerDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.utils.MD5;
import com.mu.utils.Time;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ku7PayServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private static Logger logger = LoggerFactory.getLogger(Ku7PayServlet.class);

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("utf-8");
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter writer = null;

      try {
         writer = response.getWriter();
         String apiKey = request.getParameter("api_key");
         String userId = request.getParameter("user_id");
         String serverId = request.getParameter("server_id");
         String timestamp = request.getParameter("timestamp");
         String orderID = request.getParameter("order_id");
         String money = request.getParameter("amount");
         String sign = request.getParameter("sign");
         userId = URLDecoder.decode(userId, "utf-8");
         if (checkSign(money, apiKey, orderID, userId, serverId, timestamp, sign)) {
            writer.write(pay(money, apiKey, orderID, userId, Integer.parseInt(serverId), timestamp));
         } else {
            writer.write("{\"ectcode\":-17}");
         }

         return;
      } catch (Exception var14) {
         var14.printStackTrace();
         if (writer != null) {
            writer.write("{\"ectcode\":-19}");
         }
      } finally {
         if (writer != null) {
            writer.close();
         }

      }

   }

   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      this.doGet(req, resp);
   }

   public static boolean checkSign(String money, String apiKey, String orderID, String uid, String sid, String timestamp, String sign) {
      StringBuffer sb = new StringBuffer();
      sb.append(Global.getKey("cert")).append("amount").append(money);
      sb.append("api_key").append(apiKey);
      sb.append("order_id").append(orderID);
      sb.append("server_id").append(sid);
      sb.append("timestamp").append(timestamp);
      sb.append("user_id").append(uid);
      return MD5.md5s(sb.toString()).toUpperCase().equals(sign.toUpperCase());
   }

   private static String pay(String money, String apiKey, String orderID, String userID, int sid, String timestamp) {
      try {
         long time = Long.parseLong(timestamp);
         String timeStr = Time.getTimeStr(time);
         float tmpMoney = Float.parseFloat(money);
         int tmpGold = (int)(tmpMoney * 10.0F);
         String uid = URLDecoder.decode(userID, "utf-8");
         Player player = CenterManager.getPlayerByUserName(userID, sid);
         if (player != null) {
            PlayerManager.addIngot(player, tmpGold, IngotChangeType.GMPay.getType());
            player.getUser().addPay(tmpGold, System.currentTimeMillis());
            logger.info("充值成功,充值金额" + money + "元");
            PayDBManager.intoPayLog(orderID, userID, sid, tmpMoney, tmpGold, timeStr, "", 1, "CNY");
            return "{\"ectcode\":0}";
         }

         if (PlayerDBManager.offLineUpdateIngot(uid, tmpGold, sid) == 1) {
            PayDBManager.intoPayLog(orderID, userID, sid, tmpMoney, tmpGold, timeStr, "", 1, "CNY");
            LogDBManager.addIngotLog(userID, -1L, "", tmpGold, IngotChangeType.PlatPay.getType(), 1, Global.getServerID());
            logger.info("充值成功,充值金额" + money + "元");
            return "{\"ectcode\":0}";
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

      return "{\"ectcode\":-19}";
   }

   public static void main(String[] args) {
      String key = "8a9a538c4fb63449014fee1035624692";
      String user_name = "athena";
      String server_id = "1";
      String time = String.valueOf(System.currentTimeMillis() / 1000L);
      String orderId = String.valueOf(System.currentTimeMillis());
      String apiKey = "apiKey";
      int money = 10;
      String s = key + "amount" + money + "api_key" + apiKey + "order_id" + orderId + "server_id" + server_id + "timestamp" + time + "user_id" + user_name;
      System.out.println(s);
      String sign = MD5.md5s(s).toUpperCase();
      String ip = "42.51.9.56";
      System.out.println(ip + ":7517/pay?" + "order_id=" + orderId + "&api_key=" + apiKey + "&user_id=" + user_name + "&server_id=" + server_id + "&timestamp=" + time + "&amount=" + money + "&sign=" + sign);
   }
}
