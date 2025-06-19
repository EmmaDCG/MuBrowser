package com.mu.game.model.gift;

import com.mu.config.Global;
import com.mu.executor.Executor;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.player.tips.SystemFunctionTip;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.License;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.http.message.BasicNameValuePair;

public class GiftSnManager {
   private static HashMap giftMap = new HashMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         int gift = Tools.getCellIntValue(sheet.getCell("B" + i));
         ItemDataUnit unit = new ItemDataUnit(gift, 1, true);
         giftMap.put(id, unit);
      }

   }

   public static void receiveSnGift(final Player player, final String sn) {
      try {
         if (sn.length() != 26) {
            SystemMessage.writeMessage(player, 25001);
            return;
         }

         String tmpSn = License.parseLicense(sn);
         int plat = Integer.parseInt(tmpSn.substring(0, 3));
         int id = Integer.parseInt(tmpSn.substring(4, 6));
         if (!giftMap.containsKey(id) || plat != 0 && Global.getPlatID() != plat) {
            SystemMessage.writeMessage(player, 25001);
            return;
         }

         if (player.getSnLogs().hasReceived(id)) {
            SystemMessage.writeMessage(player, 25004);
            return;
         }

         int result = checkSnBeUsed(sn);
         if (result != 1) {
            SystemMessage.writeMessage(player, result);
         } else {
            OperationResult or = player.getItemManager().addItem((ItemDataUnit)giftMap.get(id));
            if (or.getResult() == 1) {
               SystemFunctionTip.sendToClient(player, 7, or.getItemID());
               player.getSnLogs().addLog(id);
               WriteOnlyPacket packet = Executor.SaveSnReceive.toPacket(player.getID(), id);
               player.writePacket(packet);
               packet.destroy();
               packet = null;
               ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
                  public void run() {
                     GiftSnManager.saveSn(sn, player.getName(), player.getUserName(), player.getID(), player.getUser().getServerID());
                  }
               });
            } else {
               SystemMessage.writeMessage(player, or.getResult());
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
         SystemMessage.writeMessage(player, 25001);
      }

   }

   private static int checkSnBeUsed(String sn) {
      int result = 1;
      ArrayList list = new ArrayList();

      try {
         list.add(new BasicNameValuePair("type", "3"));
         list.add(new BasicNameValuePair("sn", sn));
         String[] resp = Tools.getUrlPostContent(Global.getLoginServletPath(), list);
         if (resp[0].equals("ok")) {
            if (Integer.parseInt(resp[1]) == 1) {
               result = 25003;
            }
         } else {
            result = 25002;
         }
      } catch (Exception var7) {
         var7.printStackTrace();
         result = 25002;
      } finally {
         list.clear();
      }

      return result;
   }

   private static void saveSn(String sn, String roleName, String userName, long rid, int serverId) {
      ArrayList list = new ArrayList();

      try {
         list.add(new BasicNameValuePair("type", "4"));
         list.add(new BasicNameValuePair("sn", sn));
         list.add(new BasicNameValuePair("rid", String.valueOf(rid)));
         list.add(new BasicNameValuePair("rn", roleName));
         list.add(new BasicNameValuePair("un", userName));
         list.add(new BasicNameValuePair("sid", String.valueOf(serverId)));
         Tools.getUrlPostContent(Global.getLoginServletPath(), list);
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }
}
