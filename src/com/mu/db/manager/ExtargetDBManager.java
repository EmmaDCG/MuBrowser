package com.mu.db.manager;

import com.mu.db.Pool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ExtargetDBManager {
   private static final String sqlUpdateCollect = "replace into role_extarget value(?,?,?)";
   private static final String sqlReceive = "replace into role_extarget_receive values(?,?)";
   private static final String sqlGetCollected = "select big_id,small_id from role_extarget where role_id = ?";
   private static final String sqlGetReceived = "select big_id from role_extarget_receive where role_id = ?";

   public static void saveCollect(long rid, int bid, int sid) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into role_extarget value(?,?,?)");
         ps.setLong(1, rid);
         ps.setInt(2, bid);
         ps.setInt(3, sid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveReceive(long rid, int bid) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into role_extarget_receive values(?,?)");
         ps.setLong(1, rid);
         ps.setInt(2, bid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static ArrayList getCollectedList(long rid) {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select big_id,small_id from role_extarget where role_id = ?");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int bid = rs.getInt("big_id");
            int sid = rs.getInt("small_id");
            list.add(new int[]{bid, sid});
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

   public static ArrayList getReceivedList(long rid) {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select big_id from role_extarget_receive where role_id = ?");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int bid = rs.getInt("big_id");
            list.add(bid);
         }

         rs.close();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }
}
