package com.mu.io.http.servlet.plat.qq;

import com.mu.config.Global;
import com.mu.config.MessageText;
import com.mu.game.CenterManager;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.imp.tx.bluerenew.ActivityBlueRenew;
import com.mu.game.model.activity.imp.tx.bluerenew.ActivityBlueRenewElement;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.mail.SendMailTask;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.User;
import com.mu.io.game.packet.imp.account.ChangeSelfVipIcon;
import com.mu.io.game.packet.imp.account.ChangeVipIcon;
import com.mu.io.http.servlet.plat.qq.api.APIManager;
import com.mu.utils.Time;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import com.qq.open.SnsSigCheck;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenBlueVipServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private static Logger logger = LoggerFactory.getLogger(OpenBlueVipServlet.class);

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("utf-8");
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter writer = null;

      try {
         writer = response.getWriter();
         Enumeration names = request.getParameterNames();
         HashMap params = new HashMap();

         String ts;
         String payitem;
         while(names.hasMoreElements()) {
            ts = (String)names.nextElement();
            payitem = request.getParameter(ts);
            params.put(ts, payitem);
         }

         ts = request.getParameter("ts");
         payitem = request.getParameter("payitem");
         String billno = request.getParameter("billno");
         String openid = request.getParameter("openid");
         String sid = request.getParameter("sid");
         if (check(params)) {
            String result = sendItem(payitem, openid + "_" + billno, openid, Integer.parseInt(sid), ts);
            writer.write(result);
         } else {
            writer.write("{\"ret\":4,\"msg\":\"请求参数错误：(sig)\"}");
         }

         params.clear();
         params = null;
      } catch (Exception var15) {
         var15.printStackTrace();
         if (writer != null) {
            writer.write("{\"ret\":1000,\"msg\":\"游戏服务器繁忙\"}");
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

   private static String sendItem(String payitem, String orderID, String userID, int sid, String ts) {
      try {
         String[] atomSplits = payitem.split("\\*");
         if (atomSplits.length >= 3 && ItemModel.hasItemModel(Integer.parseInt(atomSplits[0])) && Integer.parseInt(atomSplits[2]) >= 1) {
            ActivityBlueRenew renew = ActivityManager.getBlueRenew();
            if (renew != null && renew.isOpen()) {
               ActivityBlueRenewElement be = renew.getElement();
               final Player player = CenterManager.getPlayerByUserName(userID, sid);
               if (player != null) {
                  if (be != null && be.getReceiveStatus(player) != 2) {
                     int itemModelId = Integer.parseInt(atomSplits[0]);
                     int count = Integer.parseInt(atomSplits[2]);
                     OperationResult or = player.getItemManager().addItem(itemModelId, count, true, 37);
                     if (!or.isOk()) {
                        Item item = ItemTools.createItem(itemModelId, count, 2);
                        ArrayList itemList = new ArrayList();
                        itemList.add(item);
                        SendMailTask.sendMail(player, player.getID(), MessageText.getText(23101), MessageText.getText(23102), itemList);
                        itemList.clear();
                        itemList = null;
                     }

                     player.getActivityLogs().received(renew.getElement().getReceiveType(), player.getUser().getServerID(), renew.getElement().getId(), Time.getTimeStr());
                     logger.info("开通或者续费蓝钻成功");
                     ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
                        public void run() {
                           try {
                              User user = player.getUser();
                              player.getUser().getBlueVip().init(APIManager.getBlueVipInfo(user));
                              ChangeSelfVipIcon.change(player, user.getBlueVip().getBlueIcon());
                              ChangeVipIcon.changIcons(player, user.getBlueVip().getBlueIcon());
                           } catch (Exception var2) {
                              var2.printStackTrace();
                           }

                        }
                     });
                     renew.removeRole(player.getID());
                     renew.refreshIcon(player);
                     return "{\"ret\":0,\"msg\":\"OK\"}";
                  } else {
                     return "{\"ret\":4,\"msg\":\"活动已结束\"}";
                  }
               } else {
                  return "{\"ret\":1002,\"msg\":\"请先登录\"}";
               }
            } else {
               return "{\"ret\":4,\"msg\":\"活动已结束\"}";
            }
         } else {
            return "{\"ret\":4,\"msg\":\"请求参数错误：(payitem)\"}";
         }
      } catch (Exception var14) {
         var14.printStackTrace();
         return "{\"ret\":1000,\"msg\":\"游戏服务器繁忙\"}";
      }
   }
}
