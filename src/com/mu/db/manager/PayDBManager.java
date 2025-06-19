package com.mu.db.manager;

import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.db.log.atom.PayConfirmLog;
import com.mu.utils.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PayDBManager {
   private static final String insertDB = "insert into mu_pay_logs values (?,?,?,?,?,?,?,?,?,?)";
   private static final String getPay = "select gold,pay_time from mu_pay_logs where user_name = ? and server_id = ? order by pay_time asc";
   private static final String checkPay = "select count(1) from mu_pay_logs where order_id = ?";
   private static final String checkQqPay = "select count(1) from mu_pay_logs where user_name = ? and order_id = ?";
   private static final String saveConfirmLog = "REPLACE INTO mu_pay_err_log values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

   public static void intoPayLog(String orderID, String userName, int serverID, float money, int gold, String time, String des, int type, String currency) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("insert into mu_pay_logs values (?,?,?,?,?,?,?,?,?,?)");
         ps.setString(1, orderID);
         ps.setString(2, userName);
         ps.setInt(3, serverID);
         ps.setInt(4, Global.getPlatID());
         ps.setFloat(5, money);
         ps.setInt(6, gold);
         ps.setString(7, time);
         ps.setString(8, des);
         ps.setInt(9, type);
         ps.setString(10, currency);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var14) {
         var14.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static ArrayList getPayList(String userName, int serverId) {
      Connection conn = null;
      ArrayList list = new ArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select gold,pay_time from mu_pay_logs where user_name = ? and server_id = ? order by pay_time asc");
         ps.setString(1, userName);
         ps.setInt(2, serverId);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            String time = rs.getString("pay_time");
            int ingot = rs.getInt("gold");
            long[] info = new long[]{Time.getTimeStringToMills(time), (long)ingot};
            list.add(info);
         }

         rs.close();
         ps.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static boolean hasQqSameOrder(String userName, String orderId) {
      Connection conn = null;
      boolean b = false;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select count(1) from mu_pay_logs where user_name = ? and order_id = ?");
         ps.setString(1, userName);
         ps.setString(2, orderId);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            int count = rs.getInt(1);
            if (count > 0) {
               b = true;
            }
         }

         rs.close();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return b;
   }

   public static boolean hasSameOrder(String orderId) {
      Connection conn = null;
      boolean b = false;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select count(1) from mu_pay_logs where order_id = ?");
         ps.setString(1, orderId);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            int count = rs.getInt(1);
            if (count > 0) {
               b = true;
            }
         }

         rs.close();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return b;
   }

   public static void saveConfirmLog(PayConfirmLog log, int result) {
      Connection conn = null;

      try {
         conn = Pool.getGlobalLogConnection();
         PreparedStatement ps = conn.prepareStatement("REPLACE INTO mu_pay_err_log values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
         ps.setString(1, log.getOpenID());
         ps.setString(2, log.getOpenKey());
         ps.setString(3, log.getTs());
         ps.setString(4, log.getPayitem());
         ps.setString(5, log.getToken());
         ps.setString(6, log.getBillno());
         ps.setString(7, log.getZoneid());
         ps.setString(8, log.getProvide_errno());
         ps.setString(9, log.getAmt());
         ps.setString(10, log.getPayamt_coins());
         ps.setInt(11, log.getSid());
         ps.setInt(12, result);
         ps.setString(13, Time.getTimeStr());
         ps.executeUpdate();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
