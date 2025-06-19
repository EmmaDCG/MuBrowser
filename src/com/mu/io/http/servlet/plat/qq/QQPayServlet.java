package com.mu.io.http.servlet.plat.qq;

import com.mu.config.Global;
import com.mu.db.log.IngotChangeType;
import com.mu.db.log.atom.PayConfirmLog;
import com.mu.db.manager.GlobalLogDBManager;
import com.mu.db.manager.PayDBManager;
import com.mu.executor.imp.player.SavePayLogExecutor;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.task.schedule.PayConfirmTask;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import com.qq.open.SnsSigCheck;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QQPayServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private static Logger logger = LoggerFactory.getLogger(QQPayServlet.class);
   public static final String pay_ok = "{\"ret\":0,\"msg\":\"OK\"}";
   public static final String server_err = "{\"ret\":1000,\"msg\":\"游戏服务器繁忙\"}";
   public static final String login_err = "{\"ret\":1002,\"msg\":\"请先登录\"}";
   public static final String sig_err = "{\"ret\":4,\"msg\":\"请求参数错误：(sig)\"}";
   public static final String payitem_err = "{\"ret\":4,\"msg\":\"请求参数错误：(payitem)\"}";
   public static final String activity_finish = "{\"ret\":4,\"msg\":\"活动已结束\"}";

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("utf-8");
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter writer = null;
      HashMap params = new HashMap();

      try {
         writer = response.getWriter();
         Enumeration names = request.getParameterNames();

         String pubacct_payamt_coins;
         String ts;
         while(names.hasMoreElements()) {
            pubacct_payamt_coins = (String)names.nextElement();
            ts = request.getParameter(pubacct_payamt_coins);
            params.put(pubacct_payamt_coins, ts);
         }

         pubacct_payamt_coins = request.getParameter("pubacct_payamt_coins");
         ts = request.getParameter("ts");
         String payitem = request.getParameter("payitem");
         String amt = request.getParameter("amt");
         String zoneid = request.getParameter("zoneid");
         String token = request.getParameter("token");
         String payamt_coins = request.getParameter("payamt_coins");
         String billno = request.getParameter("billno");
         String sig = request.getParameter("sig");
         String openid = request.getParameter("openid");
         String sid = request.getParameter("sid");
         if (check(params)) {
            boolean hasSameOrder = PayDBManager.hasQqSameOrder(openid, billno);
            if (hasSameOrder) {
               writer.write("{\"ret\":1000,\"msg\":\"游戏服务器繁忙\"}");
               return;
            }

            String result = pay(payitem, amt, billno, openid, Integer.parseInt(sid), ts);
            if (result.endsWith("{\"ret\":0,\"msg\":\"OK\"}")) {
               Player player = CenterManager.getPlayerByUserName(openid, Integer.parseInt(sid));
               if (player != null) {
                  PayConfirmLog log = new PayConfirmLog(openid, player.getUser().getOpenKey(), ts, payitem, token, billno, zoneid, "0", amt, payamt_coins, sig, Integer.parseInt(sid));
                  PayConfirmTask.addPayConfirm(log);
               }
            }

            writer.write(result);
         } else {
            writer.write("{\"ret\":4,\"msg\":\"请求参数错误：(sig)\"}");
         }
      } catch (Exception var24) {
         var24.printStackTrace();
      } finally {
         if (writer != null) {
            writer.close();
         }

         params.clear();
         params = null;
      }

   }

   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      this.doGet(req, resp);
   }

   public static boolean check(HashMap request) {
      try {
         String sig = (String)request.get("sig");
         String url_path = (String)request.remove("url_path");
         String method = (String)request.remove("postMethod");
         request.remove("sid");
         return SnsSigCheck.verifySig(method, url_path, request, Global.appkey + "&", sig);
      } catch (Exception var4) {
         var4.printStackTrace();
         return false;
      }
   }

   private static String pay(String payitem, String amt, String orderID, String userID, int sid, String ts) {
      try {
         Player player = CenterManager.getPlayerByUserName(userID, sid);
         if (player == null) {
            logger.info("未找到玩家 userID = " + userID + " , sid = " + sid);
            return "{\"ret\":1002,\"msg\":\"请先登录\"}";
         } else {
            String[] itemSplits = payitem.split("\\*");
            if (itemSplits.length >= 3 && Integer.parseInt(itemSplits[0]) >= 1 && Integer.parseInt(itemSplits[2]) >= 1) {
               int glod = Integer.parseInt(itemSplits[0]) * Integer.parseInt(itemSplits[2]);
               long time = Long.parseLong(ts) * 1000L;
               String timeStr = Time.getTimeStr(time);
               String uid = URLDecoder.decode(userID, "utf-8");
               float tmpMoney = Float.parseFloat(amt);
               PlayerManager.addIngot(player, glod, IngotChangeType.PlatPay.getType());
               player.getUser().addPay(glod, time);
               logger.info("充值成功,充值金额" + glod + "钻石，消耗" + amt + " Q豆");
               SavePayLogExecutor.savePayLog(orderID, uid, sid, tmpMoney, glod, timeStr, player);
               doOtherAfterPay(player, orderID, uid, sid, tmpMoney, glod, timeStr, player.getID(), player.getLevel(), player.getName());
               return "{\"ret\":0,\"msg\":\"OK\"}";
            } else {
               return "{\"ret\":4,\"msg\":\"请求参数错误：(payitem)\"}";
            }
         }
      } catch (Exception var15) {
         var15.printStackTrace();
         return "{\"ret\":1000,\"msg\":\"游戏服务器繁忙\"}";
      }
   }

   private static void doOtherAfterPay(final Player player, final String orderID, final String userName, final int serverID, final float money, final int gold, final String time, final long roleId, final int roleLevel, final String name) {
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            GlobalLogDBManager.intoPayLog(orderID, userName, serverID, money, gold, time, "", 1, "CNY", roleId, roleLevel, name);
            UpdateMenu.update(player, 16);
            UpdateMenu.update(player, 29);
            UpdateMenu.update(player, 34);
         }
      });
   }

   public static void sendToLoginServer(String billno, String openid, String sid, String payitem, String amt, String pubacct_payamt_coins, String ts, String zoneid) {
      ArrayList list = new ArrayList();
      list.add(new BasicNameValuePair("billno", billno));
      list.add(new BasicNameValuePair("openid", openid));
      list.add(new BasicNameValuePair("sid", sid));
      list.add(new BasicNameValuePair("payitem", payitem));
      list.add(new BasicNameValuePair("amt", amt));
      list.add(new BasicNameValuePair("pubacct_payamt_coins", pubacct_payamt_coins));
      list.add(new BasicNameValuePair("ts", ts));
      list.add(new BasicNameValuePair("zoneid", zoneid));
      String s = Global.getLoginServletPath() + "/pay";
      Tools.getUrlPostContent(s, list);
   }
}
