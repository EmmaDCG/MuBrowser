package com.mu.game.model.unit.player.fcm;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.User;
import com.mu.io.game.packet.imp.sys.OpenFcmPannel;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import jxl.Sheet;
import jxl.Workbook;

public class FcmManager {
   public static final int Fcm_Hour1 = 1;
   public static final int Fcm_Hour2 = 2;
   public static final int Fcm_Hour3 = 3;
   public static final int Fcm_Hour3AHalf = 4;
   public static final int Fcm_Hour4 = 5;
   public static final int Fcm_Hour4AHalf = 6;
   public static final int Fcm_Hour5 = 7;
   public static final int Fcm_Hour5Above = 8;
   public static final long Time_Hour1 = 3600L;
   public static final long Time_Hour2 = 7200L;
   public static final long Time_Hour3 = 10800L;
   public static final long Time_Hour3AHalf = 12600L;
   public static final long Time_Hour4 = 14400L;
   public static final long Time_Hour4AHalf = 16200L;
   public static final long Time_Hour5 = 18000L;
   public static final long Time_Hour5Above = 900L;
   private static HashMap fcmMap = new HashMap();
   private static ConcurrentHashMap pushMap = new ConcurrentHashMap(16, 0.75F, 2);

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         FcmInfo info = new FcmInfo(Tools.getCellIntValue(sheet.getCell("A" + i)));
         info.setMsg1(Tools.getCellValue(sheet.getCell("C" + i)));
         info.setMsg2(Tools.getCellValue(sheet.getCell("D" + i)));
         info.setMsg3(Tools.getCellValue(sheet.getCell("E" + i)));
         fcmMap.put(info.getId(), info);
      }

   }

   public static void removePushInfo(long id) {
      pushMap.remove(id);
   }

   public static PlayerPushInfo getPushInfo(long id) {
      return (PlayerPushInfo)pushMap.get(id);
   }

   public static void doPush(Player player, long now) {
      User user = player.getUser();
      int onlineTime = user.getOnlineTime();
      if ((long)onlineTime >= 3600L) {
         PlayerPushInfo pi = (PlayerPushInfo)pushMap.get(player.getID());
         FcmInfo info = null;
         if (pi == null) {
            pi = new PlayerPushInfo(player.getID());
            pushMap.put(pi.getId(), pi);
         }

         int pushId = pi.getPushId();
         if ((long)onlineTime < 7200L && pushId < 1) {
            pi.setPushId(1);
            info = (FcmInfo)fcmMap.get(Integer.valueOf(1));
            OpenFcmPannel.open(player, info.getMsg1(), info.getMsg2(), info.getMsg3());
         } else if ((long)onlineTime >= 7200L && (long)onlineTime < 10800L && pushId < 2) {
            pi.setPushId(2);
            info = (FcmInfo)fcmMap.get(Integer.valueOf(2));
            OpenFcmPannel.open(player, info.getMsg1(), info.getMsg2(), info.getMsg3());
         } else if ((long)onlineTime >= 10800L && (long)onlineTime < 12600L && pushId < 3) {
            pi.setPushId(3);
            info = (FcmInfo)fcmMap.get(Integer.valueOf(3));
            OpenFcmPannel.open(player, info.getMsg1(), info.getMsg2(), info.getMsg3());
         } else if ((long)onlineTime >= 12600L && (long)onlineTime < 14400L && pushId < 4) {
            pi.setPushId(4);
            info = (FcmInfo)fcmMap.get(Integer.valueOf(4));
            OpenFcmPannel.open(player, info.getMsg1(), info.getMsg2(), info.getMsg3());
         } else if ((long)onlineTime >= 14400L && (long)onlineTime < 16200L && pushId < 5) {
            pi.setPushId(5);
            info = (FcmInfo)fcmMap.get(Integer.valueOf(5));
            OpenFcmPannel.open(player, info.getMsg1(), info.getMsg2(), info.getMsg3());
         } else if ((long)onlineTime >= 16200L && (long)onlineTime < 18000L && pushId < 6) {
            pi.setPushId(6);
            info = (FcmInfo)fcmMap.get(Integer.valueOf(6));
            OpenFcmPannel.open(player, info.getMsg1(), info.getMsg2(), info.getMsg3());
         } else if ((long)onlineTime >= 18000L) {
            if (pushId == 6) {
               pi.setPushId(7);
               info = (FcmInfo)fcmMap.get(Integer.valueOf(7));
               OpenFcmPannel.open(player, info.getMsg1(), info.getMsg2(), info.getMsg3());
            } else {
               int inter = (int)((now - pi.getLastPushTime()) / 1000L);
               if ((long)inter > 900L) {
                  pi.setPushId(8);
                  int hour = onlineTime / 3600;
                  int min = (onlineTime - hour * 3600) / 60;
                  info = (FcmInfo)fcmMap.get(Integer.valueOf(8));
                  String msg2 = info.getMsg2().replace("%h%", String.valueOf(hour)).replace("%m%", String.valueOf(min));
                  OpenFcmPannel.open(player, info.getMsg1(), msg2, info.getMsg3());
               }
            }
         }

      }
   }
}
