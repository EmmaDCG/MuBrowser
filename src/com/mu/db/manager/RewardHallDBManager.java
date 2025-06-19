package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.io.game.packet.imp.rewardhall.online.OnlineRewardInit;
import com.mu.io.game.packet.imp.rewardhall.sign.SignInit;
import com.mu.io.game.packet.imp.rewardhall.vitality.VitalityInit;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RewardHallDBManager {
   public static final String SQL_SELECT_SIGN = "SELECT * FROM role_sign WHERE roleId=? ";
   public static final String SQL_REPLACE_SIGN = "replace into role_sign value (?,?,?,?,?,  ?) ";
   public static final String SQL_SELECT_VITALITY = "SELECT * FROM role_vitality WHERE roleId=? ";
   public static final String SQL_SELECT_VITALITY_TASK = "SELECT * FROM role_vitality_task WHERE roleId=? ";
   public static final String SQL_REPLACE_VITALITY = "replace into role_vitality value (?,?,?) ";
   public static final String SQL_REPLACE_VITALITY_TASK = "replace into role_vitality_task value (?,?,?) ";
   public static final String SQL_CLEAR_VITALITY_TASK = "delete from role_vitality_task where roleId=? ";
   public static final String SQL_SELECT_ONLINE = "SELECT * FROM role_online_reward WHERE roleId=? ";
   public static final String SQL_REPLACE_ONLINE = "replace into role_online_reward value (?,?,?,?,?, ?) ";

   public static SignInit initSign(long roleId) {
      SignInit packet = new SignInit(46100, (byte[])null);
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("SELECT * FROM role_sign WHERE roleId=? ");
         ps.setLong(1, roleId);
         ResultSet rs = ps.executeQuery();
         boolean has = rs.next();
         packet.writeBoolean(has);
         if (has) {
            packet.writeInt(rs.getInt("round"));
            packet.writeLong(rs.getLong("roundStart"));
            packet.writeLong(rs.getLong("signTime"));
            InputStream in = rs.getBinaryStream("timeSet");
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            in.close();
            packet.writeByte(bytes.length / 8);
            packet.writeBytes(bytes, 0, bytes.length / 8 * 8);
            in = rs.getBinaryStream("rewardSet");
            bytes = new byte[in.available()];
            in.read(bytes);
            in.close();
            packet.writeByte(bytes.length);
            packet.writeBytes(bytes);
         }

         rs.close();
         Pool.closeStatment(ps);
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

      return packet;
   }

   public static OnlineRewardInit initOnline(long roleId) {
      OnlineRewardInit packet = new OnlineRewardInit(46300, (byte[])null);
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("SELECT * FROM role_online_reward WHERE roleId=? ");
         ps.setLong(1, roleId);
         ResultSet rs = ps.executeQuery();
         boolean has = rs.next();
         packet.writeBoolean(has);
         if (has) {
            packet.writeUTF(rs.getString("dayReceiveStr"));
            packet.writeLong(rs.getLong("weekReceiveTime"));
            packet.writeInt(rs.getInt("weekIndex"));
            packet.writeInt(rs.getInt("weekSeconds"));
            packet.writeInt(rs.getInt("weekAccumulative"));
         }

         rs.close();
         Pool.closeStatment(ps);
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

      return packet;
   }

   public static VitalityInit initVitality(long roleId) {
      VitalityInit packet = new VitalityInit(46200, (byte[])null);
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("SELECT * FROM role_vitality WHERE roleId=? ");
         ps.setLong(1, roleId);
         ResultSet rs = ps.executeQuery();
         boolean has = rs.next();
         packet.writeBoolean(has);
         if (has) {
            packet.writeInt(rs.getInt("vitality"));
            InputStream in = rs.getBinaryStream("rewardSet");
            byte[] bytes = new byte[in.available()];
            in.read(bytes, 0, bytes.length);
            in.close();
            packet.writeByte(bytes.length);
            packet.writeBytes(bytes);
         }

         rs.close();
         Pool.closeStatment(ps);
         ps = conn.prepareStatement("SELECT * FROM role_vitality_task WHERE roleId=? ");
         ps.setLong(1, roleId);
         rs = ps.executeQuery();

         while(rs.next()) {
            packet.writeByte(rs.getInt("taskId"));
            packet.writeInt(rs.getInt("rate"));
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

      return packet;
   }

   public static void replaceSign(long roleId, byte[] timeSet, int round, long roundStart, long signTime, byte[] rewardSet) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_sign value (?,?,?,?,?,  ?) ");
         ps.setLong(1, roleId);
         ps.setBytes(2, timeSet);
         ps.setInt(3, round);
         ps.setLong(4, roundStart);
         ps.setLong(5, signTime);
         ps.setBytes(6, rewardSet);
         ps.execute();
      } catch (Exception var15) {
         var15.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void replaceOnline(long roleId, String dayReceiveStr, long weekReceiveTime, int weekIndex, int weekSeconds, int weekAccumulative) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_online_reward value (?,?,?,?,?, ?) ");
         ps.setLong(1, roleId);
         ps.setString(2, dayReceiveStr);
         ps.setLong(3, weekReceiveTime);
         ps.setInt(4, weekIndex);
         ps.setInt(5, weekSeconds);
         ps.setInt(6, weekAccumulative);
         ps.execute();
      } catch (Exception var14) {
         var14.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void replaceVitality(long roleId, int vitality, byte[] rewardSet) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_vitality value (?,?,?) ");
         ps.setLong(1, roleId);
         ps.setInt(2, vitality);
         ps.setBytes(3, rewardSet);
         ps.execute();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void replaceVitalityTask(long roleId, int taskId, int rate) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_vitality_task value (?,?,?) ");
         ps.setLong(1, roleId);
         ps.setInt(2, taskId);
         ps.setInt(3, rate);
         ps.execute();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void clearVitalityTask(long roleId) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("delete from role_vitality_task where roleId=? ");
         ps.setLong(1, roleId);
         ps.execute();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }
}
