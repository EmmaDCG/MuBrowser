package com.mu.io.http.servlet.plat.baidu;

import com.mu.config.Global;
import com.mu.db.log.IngotChangeType;
import com.mu.db.log.LogDBManager;
import com.mu.db.manager.PayDBManager;
import com.mu.db.manager.PlayerDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.utils.MD5;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaiDuPayServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private static Logger logger = LoggerFactory.getLogger(BaiDuPayServlet.class);

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("utf-8");
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter writer = null;

      try {
         writer = response.getWriter();
         String result = request.getParameter("result");
         String apiKey = request.getParameter("api_key");
         String userId = request.getParameter("user_id");
         String serverId = request.getParameter("server_id");
         String timestamp = request.getParameter("timestamp");
         String orderID = request.getParameter("order_id");
         String wanbaOid = request.getParameter("wanba_oid");
         String money = request.getParameter("amount");
         String currency = request.getParameter("currency");
         String backSend = request.getParameter("back_send");
         String sign = request.getParameter("sign");
         userId = URLDecoder.decode(userId, "utf-8");
         if (!result.equals("1")) {
            writer.write("ERROR");
            return;
         } else {
            if (checkSign(money, apiKey, backSend, currency, orderID, result, userId, serverId, timestamp, wanbaOid, sign)) {
               writer.write(pay(money, apiKey, backSend, currency, orderID, result, userId, Integer.parseInt(serverId), timestamp, wanbaOid));
            } else {
               writer.write("ERROR");
            }

            return;
         }
      } catch (Exception var18) {
         var18.printStackTrace();
         if (writer != null) {
            writer.write("ERROR_-1");
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

   public static boolean checkSign(String money, String apiKey, String backSend, String currency, String orderID, String result, String uid, String sid, String timestamp, String wanbaOid, String sign) {
      StringBuffer sb = new StringBuffer();
      sb.append(Global.getKey("cert")).append("amount").append(money);
      sb.append("api_key").append(apiKey);
      sb.append("back_send").append(backSend);
      sb.append("currency").append(currency);
      sb.append("order_id").append(orderID);
      sb.append("result").append(result);
      sb.append("server_id").append(sid);
      sb.append("timestamp").append(timestamp);
      sb.append("user_id").append(uid);
      sb.append("wanba_oid").append(wanbaOid);
      return MD5.md5s(sb.toString()).toUpperCase().equals(sign);
   }

   private static String pay(String money, String apiKey, String backSend, String currency, String orderID, String result, String userID, int sid, String timestamp, String wanbaOid) {
      try {
         float tmpMoney = Float.parseFloat(money);
         int tmpGold = (int)(tmpMoney * 10.0F);
         String uid = URLDecoder.decode(userID, "utf-8");
         Player player = CenterManager.getPlayerByUserName(userID, sid);
         if (player != null) {
            PlayerManager.addIngot(player, tmpGold, IngotChangeType.GMPay.getType());
            player.getUser().addPay(tmpGold, System.currentTimeMillis());
            logger.info("充值成功,充值金额" + money + "元");
            PayDBManager.intoPayLog(orderID, userID, sid, tmpMoney, tmpGold, timestamp, wanbaOid, 1, "CNY");
            return "<!--recv=ok-->";
         }

         if (PlayerDBManager.offLineUpdateIngot(uid, tmpGold, sid) == 1) {
            PayDBManager.intoPayLog(orderID, userID, sid, tmpMoney, tmpGold, timestamp, wanbaOid, 1, "CNY");
            LogDBManager.addIngotLog(userID, -1L, "", tmpGold, IngotChangeType.PlatPay.getType(), -1, Global.getServerID());
            logger.info("充值成功,充值金额" + money + "元");
            return "<!--recv=ok-->";
         }
      } catch (Exception var14) {
         var14.printStackTrace();
      }

      return "ERROR";
   }
}
