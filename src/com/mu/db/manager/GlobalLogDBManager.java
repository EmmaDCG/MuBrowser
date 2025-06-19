package com.mu.db.manager;

import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class GlobalLogDBManager {
   private static final String sqlRegistUser = "insert ignore into mu_user values (?,?,?,?,?,?,?)";
   private static final String sqlUpdteIngot = "update mu_user set ingot = ? where user_name = ? and server_id = ?";
   private static final String sqlInsertRole = "replace into mu_role values (?,?,?,?,?,?,?,?,?)";
   private static final String sqlRoleOnlineLog = " insert into role_online_log values (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE online_time = online_time + ?";
   private static final String saveOnlineLog = "insert into mu_online_log values (?,?,?,?,?)";
   private static final String saveLogInOutLog = " insert into mu_longinout_log values (?,?,?,?,?,?,?,?)";
   private static final String insertPayLog = "insert into mu_pay_logs values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   private static final String updateBindIngot = "update mu_role set bind_ingot = ? where role_id = ?";
   public static final String sqlGetBadWords = "select * from mu_badword";

   public static void registerUser(String userName, int serverId, String date, String vipTag, String ip) {
      Connection conn = null;

      try {
         conn = Pool.getGlobalLogConnection();
         PreparedStatement ps = conn.prepareStatement("insert ignore into mu_user values (?,?,?,?,?,?,?)");
         ps.setString(1, userName);
         ps.setInt(2, Global.getParserID());
         ps.setInt(3, serverId);
         ps.setString(4, date);
         ps.setString(5, vipTag);
         ps.setString(6, ip);
         ps.setInt(7, 0);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static ArrayList getBadWordList() {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getGlobalLogConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("select * from mu_badword");

         while(rs.next()) {
            list.add(rs.getString("word"));
         }

         rs.close();
         st.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void saveIngot(int ingot, String userName, int serverId) {
      Connection conn = null;

      try {
         conn = Pool.getGlobalLogConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_user set ingot = ? where user_name = ? and server_id = ?");
         ps.setInt(1, ingot);
         ps.setString(2, userName);
         ps.setInt(3, serverId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveOnline(int num, int ipNum, String date, int newUser, int serverId) {
      Connection conn = null;

      try {
         conn = Pool.getGlobalLogConnection();
         PreparedStatement ps = conn.prepareStatement("insert into mu_online_log values (?,?,?,?,?)");
         ps.setString(1, date);
         ps.setInt(2, serverId);
         ps.setInt(3, num);
         ps.setInt(4, ipNum);
         ps.setInt(5, newUser);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void insertRole(long rid, String userName, String name, int serverId, String createTime, int level, int pro, int proLevel) {
      Connection conn = null;

      try {
         conn = Pool.getGlobalLogConnection();
         PreparedStatement ps = conn.prepareStatement("replace into mu_role values (?,?,?,?,?,?,?,?,?)");
         ps.setLong(1, rid);
         ps.setString(2, userName);
         ps.setString(3, name);
         ps.setInt(4, serverId);
         ps.setString(5, createTime);
         ps.setInt(6, level);
         ps.setInt(7, pro);
         ps.setInt(8, proLevel);
         ps.setInt(9, 0);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var14) {
         var14.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void updateBindIngot(long rid, int bindIngot) {
      Connection conn = null;

      try {
         conn = Pool.getGlobalLogConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_role set bind_ingot = ? where role_id = ?");
         ps.setInt(1, bindIngot);
         ps.setLong(2, rid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveLogInLogWhenZeroTime() {
      Connection conn = null;

      try {
         conn = Pool.getGlobalLogConnection();
         PreparedStatement ps = conn.prepareStatement(" insert into mu_longinout_log values (?,?,?,?,?,?,?,?)");
         long now = System.currentTimeMillis() + 1000L;
         String time = Time.getTimeStr(now);
         Iterator it = CenterManager.getAllPlayerIterator();

         while(it.hasNext()) {
            Player player = (Player)it.next();
            String ip = player.getUser().getRemoteIp();
            ps.setString(1, player.getUserName());
            ps.setLong(2, player.getID());
            ps.setInt(3, player.getUser().getServerID());
            ps.setString(4, player.getName());
            ps.setInt(5, 2);
            ps.setString(6, time);
            ps.setInt(7, player.getLevel());
            ps.setString(8, ip == null ? "" : ip);
            ps.addBatch();
         }

         ps.executeBatch();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveLogInOutLog(String userName, long rid, int serverId, String name, int type, int level, String ip) {
      Connection conn = null;

      try {
         conn = Pool.getGlobalLogConnection();
         PreparedStatement ps = conn.prepareStatement(" insert into mu_longinout_log values (?,?,?,?,?,?,?,?)");
         long now = System.currentTimeMillis() + 1000L;
         String time = Time.getTimeStr(now);
         ps.setString(1, userName);
         ps.setLong(2, rid);
         ps.setInt(3, serverId);
         ps.setString(4, name);
         ps.setInt(5, type);
         ps.setString(6, time);
         ps.setInt(7, level);
         ps.setString(8, ip);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var16) {
         var16.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveRoleOnlineLog(int second) {
      Connection conn = null;

      try {
         conn = Pool.getGlobalLogConnection();
         PreparedStatement ps = conn.prepareStatement(" insert into role_online_log values (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE online_time = online_time + ?");
         long now = Time.getDayLong();
         String time = Time.getTimeStr(System.currentTimeMillis(), "yyyy-MM-dd");
         Iterator it = CenterManager.getAllPlayerIterator();

         while(it.hasNext()) {
            Player player = (Player)it.next();
            ps.setLong(1, player.getID());
            ps.setInt(2, player.getUser().getServerID());
            ps.setLong(3, now);
            ps.setString(4, time);
            ps.setInt(5, second);
            ps.setString(6, player.getUserName());
            ps.setString(7, player.getName());
            ps.setInt(8, second);
            ps.addBatch();
         }

         ps.executeBatch();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void intoPayLog(String orderID, String userName, int serverID, float money, int gold, String time, String des, int type, String currency, long id, int level, String name) {
      Connection conn = null;

      try {
         conn = Pool.getGlobalLogConnection();
         PreparedStatement ps = conn.prepareStatement("insert into mu_pay_logs values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
         ps.setLong(11, id);
         ps.setInt(12, level);
         ps.setInt(13, Global.getServerID());
         ps.setString(14, name);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var18) {
         var18.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
