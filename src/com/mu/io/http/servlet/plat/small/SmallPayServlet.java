package com.mu.io.http.servlet.plat.small;

import com.mu.db.log.IngotChangeType;
import com.mu.db.manager.GlobalLogDBManager;
import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.imp.player.SavePayLogExecutor;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.utils.MD5;
import com.mu.utils.Time;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmallPayServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private static final String key = "bb74afdf84a84a1c6ff3b5bb05b93eae";
   private static Logger logger = LoggerFactory.getLogger(SmallPayServlet.class);

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("utf-8");
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter writer = null;

      try {
         writer = response.getWriter();
         String time = request.getParameter("time");
         String server_id = request.getParameter("server_id");
         String order_id = request.getParameter("order_id");
         String user_name = request.getParameter("user_name");
         String money = request.getParameter("money");
         String sign = request.getParameter("sign");
         String str = "passport=" + user_name + "&sid=" + server_id + "&money=" + money + "&billno=" + order_id + "&time=" + time + "&key=" + "bb74afdf84a84a1c6ff3b5bb05b93eae";
         if (MD5.md5s(str).toLowerCase().equals(sign)) {
            writer.write(pay(server_id, order_id, user_name, money, time));
         } else {
            writer.write(getResult(17));
         }

         return;
      } catch (Exception var14) {
         var14.printStackTrace();
         if (writer != null) {
            writer.write(getResult(-18));
         }
      } finally {
         if (writer != null) {
            writer.close();
         }

      }

   }

   private static String getResult(int state) {
      return "{\"status\":" + state + "}";
   }

   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      this.doGet(req, resp);
   }

   private static String pay(String server_id, String orderID, String user_name, String money, String ts) {
      try {
         Player player = CenterManager.getPlayerByUserName(user_name, Integer.parseInt(server_id));
         long time = Long.parseLong(ts) * 1000L;
         String timeStr = Time.getTimeStr(time);
         String uid = URLDecoder.decode(user_name, "utf-8");
         float tmpMoney = Float.parseFloat(money);
         int tmpGold = (int)(tmpMoney * 10.0F);
         if (player != null) {
            PlayerManager.addIngot(player, tmpGold, IngotChangeType.PlatPay.getType());
            player.getUser().addPay(tmpGold, time);
            logger.info("充值成功,充值金额" + tmpGold + "钻石");
            SavePayLogExecutor.savePayLog(orderID, uid, Integer.parseInt(server_id), tmpMoney, tmpGold, timeStr, player);
            doOtherAfterPay(player, orderID, uid, Integer.parseInt(server_id), tmpMoney, tmpGold, timeStr, player.getID(), player.getLevel(), player.getName());
            return getResult(1);
         } else {
            PlayerDBManager.saveOffLinePlayerIngot(uid, Integer.parseInt(server_id), tmpGold);
            GlobalLogDBManager.saveIngot(tmpGold, uid, Integer.parseInt(server_id));
            return getResult(1);
         }
      } catch (Exception var12) {
         var12.printStackTrace();
         return getResult(-18);
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
}
