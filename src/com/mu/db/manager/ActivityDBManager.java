package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityRoleLogs;
import com.mu.game.model.activity.imp.kfhd.boss.ActivityBoss;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ActivityDBManager {
   private static final String insertRoleLogs = "insert into role_activity_logs values(?,?,?)";
   private static final String getRoleLogs = "select * from role_activity_logs where role_id = ?";
   private static final String insertUserLogs = "insert into user_activity_logs values(?,?,?,?)";
   private static final String getUserLogs = "select reward_id,receive_date from user_activity_logs where user_name = ? and server_id = ?";
   private static final String getServerLimit = "select * from mu_activity_limit";
   private static final String saveServerLimit = "replace into mu_activity_limit values(?,?)";
   private static final String initBossKill = "select boss_id,killer_id from mu_kill_boss_record where kill_time between ? and ?";

   public static void insertRoleReceiveLogs(long rid, int eid, String date) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("insert into role_activity_logs values(?,?,?)");
         ps.setLong(1, rid);
         ps.setInt(2, eid);
         ps.setString(3, date);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void insertUserReceiveLogs(String userName, int serverId, int eid, String date) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("insert into user_activity_logs values(?,?,?,?)");
         ps.setString(1, userName);
         ps.setInt(2, serverId);
         ps.setInt(3, eid);
         ps.setString(4, date);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void initKillBossLog(ActivityBoss ab) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select boss_id,killer_id from mu_kill_boss_record where kill_time between ? and ?");
         ps.setLong(1, ab.getOpenDate().getTime());
         ps.setLong(2, ab.getCloseDate().getTime());
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int bossId = rs.getInt("boss_id");
            long killer = rs.getLong("killer_id");
            ab.addKillRecord(bossId, killer);
         }

         rs.close();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void initServerLimit() {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("select * from mu_activity_limit");

         while(rs.next()) {
            int eid = rs.getInt("element_id");
            int num = rs.getInt("left_num");
            ActivityManager.putServerLeft(eid, num);
         }

         rs.close();
         st.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveServerLimit(int eid, int num) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into mu_activity_limit values(?,?)");
         ps.setInt(1, eid);
         ps.setInt(2, num);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static ArrayList getUserLogs(String userName, int serverId) {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select reward_id,receive_date from user_activity_logs where user_name = ? and server_id = ?");
         ps.setString(1, userName);
         ps.setInt(2, serverId);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int eid = rs.getInt("reward_id");
            String date = rs.getString("receive_date");
            list.add(new String[]{String.valueOf(eid), date});
         }

         rs.close();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static ArrayList getRoleLogs(long rid) {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select * from role_activity_logs where role_id = ?");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int eid = rs.getInt("reward_id");
            String date = rs.getString("receive_date");
            ActivityRoleLogs log = new ActivityRoleLogs();
            log.setDate(date);
            log.setEid(eid);
            log.setRid(rid);
            list.add(log);
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
}
