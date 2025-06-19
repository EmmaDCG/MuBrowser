package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SpiritDBManager {
   private static String sqlSearchSpirit = "SELECT * FROM role_spirit where role_id = ?";
   private static String sqlUpdateSpirit = "REPLACE INTO role_spirit VALUES(?,?,?,?,?,?,?)";
   private static String sqlSearchHallows = "SELECT * FROM role_hallows where role_id = ?";
   private static String sqlUpdateHallows = "REPLACE INTO role_hallows VALUES(?,?,?)";
   private static String sqlUpdateLuckyTurnTable = "REPLACE INTO user_lucky_turn_table VALUES(?,?,?)";
   private static String sqlSearchLuckyTurnTable = "SELECT * FROM user_lucky_turn_table WHERE user_name = ? AND server_id = ?";

   public static String[] searchSpirit(long roleID) {
      Connection conn = Pool.getConnection();
      String[] entries = new String[]{"1", "1", "0", "0", "", ""};

      try {
         PreparedStatement ps = conn.prepareStatement(sqlSearchSpirit);
         ps.setLong(1, roleID);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            int rank = rs.getInt("rank");
            int level = rs.getInt("level");
            long exp = rs.getLong("exp");
            int ingotRefineCount = rs.getInt("ingot_refine_count");
            String itemCount = rs.getString("item_map");
            String conStr = rs.getString("condition_str");
            entries[0] = String.valueOf(rank);
            entries[1] = String.valueOf(level);
            entries[2] = String.valueOf(exp);
            entries[3] = String.valueOf(ingotRefineCount);
            entries[4] = itemCount;
            entries[5] = conStr;
         }

         rs.close();
         ps.close();
      } catch (Exception var16) {
         var16.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return entries;
   }

   public static void saveSpirit(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement(sqlUpdateSpirit);
         long rid = packet.readLong();
         int rank = packet.readByte();
         int level = packet.readShort();
         long exp = packet.readLong();
         int ingotRefineCount = packet.readInt();
         String itemCountStr = packet.readUTF();
         String conStr = packet.readUTF();
         ps.setLong(1, rid);
         ps.setInt(2, rank);
         ps.setInt(3, level);
         ps.setLong(4, exp);
         ps.setInt(5, ingotRefineCount);
         ps.setString(6, itemCountStr);
         ps.setString(7, conStr);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var15) {
         var15.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static int[] searchHallows(long roleID) {
      Connection conn = Pool.getConnection();
      int[] entries = new int[]{1, 0};

      try {
         PreparedStatement ps = conn.prepareStatement(sqlSearchHallows);
         ps.setLong(1, roleID);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            int rank = rs.getInt("rank");
            int level = rs.getInt("level");
            entries[0] = rank;
            entries[1] = level;
         }

         rs.close();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return entries;
   }

   public static void saveHallows(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement(sqlUpdateHallows);
         long rid = packet.readLong();
         int rank = packet.readShort();
         int level = packet.readShort();
         ps.setLong(1, rid);
         ps.setInt(2, rank);
         ps.setLong(3, (long)level);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static int searchLuckyTurnTable(String userName, int serverID) {
      Connection conn = Pool.getConnection();
      int count = 0;

      try {
         PreparedStatement ps = conn.prepareStatement(sqlSearchLuckyTurnTable);
         ps.setString(1, userName);
         ps.setInt(2, serverID);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            count = rs.getInt("count");
         }

         rs.close();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return count;
   }

   public static void saveLuckyTurnTable(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement(sqlUpdateLuckyTurnTable);
         String userName = packet.readUTF();
         int serverID = packet.readInt();
         int count = packet.readByte();
         ps.setString(1, userName);
         ps.setInt(2, serverID);
         ps.setInt(3, count);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
