package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.unit.player.dun.DunLogs;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DungeonDBManager {
   private static final String getDungeonLogs = "select * from role_dungeon_logs where role_id = ? and save_day = ?";
   private static final String getDungeonTotalLogs = "select * from role_dungeon_total_logs where role_id = ? ";
   private static final String updateDungeonLogs = "replace into role_dungeon_logs values (?,?,?,?,?,?,?,?,?,?)";
   private static final String updateDungeonTotalLogs = "replace into role_dungeon_total_logs values (?,?,?,?)";
   private static final String updateDungeonReceived = "update role_dungeon_logs set has_received = 1,base_exp = 0, base_money = 0 where role_id = ? and dun_id = ? and dun_small_id = ? and save_day = ?";
   private static final String updateDungeonUnReceived = "update role_dungeon_logs set has_received = 0,base_exp = ?, base_money = ? where role_id = ? and dun_id = ? and dun_small_id = ? and save_day = ?";

   public static ArrayList getDungeonlogs(long rid) {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select * from role_dungeon_logs where role_id = ? and save_day = ?");
         ps.setLong(1, rid);
         ps.setLong(2, Time.getDayLong());
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int dunId = rs.getInt("dun_id");
            int smallId = rs.getInt("dun_small_id");
            int finishTimes = rs.getInt("finish_times");
            long lastFinishTime = rs.getLong("last_finish_time");
            long saveDay = rs.getLong("save_day");
            long exp = rs.getLong("base_exp");
            int money = rs.getInt("base_money");
            boolean received = rs.getInt("has_received") == 1;
            int vipLevel = rs.getInt("vip_level");
            DunLogs log = new DunLogs(dunId);
            log.setFinishTimes(finishTimes);
            log.setSmallId(smallId);
            log.setLastFinishTime(lastFinishTime);
            log.setSaveDay(saveDay);
            log.setBaseExp(exp);
            log.setBaseMoney(money);
            log.setHasReceived(received);
            log.setVipLevel(vipLevel);
            list.add(log);
         }

         rs.close();
         ps.close();
      } catch (Exception var22) {
         var22.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static ArrayList getDungeonTotallogs(long rid) {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select * from role_dungeon_total_logs where role_id = ? ");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int dunId = rs.getInt("dun_id");
            int smallId = rs.getInt("dun_small_id");
            int finishTimes = rs.getInt("finish_times");
            int[] log = new int[]{dunId, smallId, finishTimes};
            list.add(log);
         }

         rs.close();
         ps.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void saveLog(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         long rid = packet.readLong();
         int dunId = packet.readUnsignedByte();
         int smallId = packet.readUnsignedShort();
         int finishTimes = packet.readInt();
         long lastFinishTime = packet.readLong();
         long saveDay = packet.readLong();
         long exp = packet.readLong();
         int money = packet.readInt();
         int received = packet.readBoolean() ? 1 : 0;
         int vipLevel = packet.readInt();
         PreparedStatement ps = conn.prepareStatement("replace into role_dungeon_logs values (?,?,?,?,?,?,?,?,?,?)");
         ps.setLong(1, rid);
         ps.setInt(2, dunId);
         ps.setInt(3, smallId);
         ps.setInt(4, finishTimes);
         ps.setLong(5, lastFinishTime);
         ps.setLong(6, saveDay);
         ps.setLong(7, exp);
         ps.setInt(8, money);
         ps.setInt(9, received);
         ps.setInt(10, vipLevel);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var20) {
         var20.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveTotalLog(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         long rid = packet.readLong();
         int dunId = packet.readUnsignedByte();
         int smallId = packet.readUnsignedShort();
         int times = packet.readInt();
         PreparedStatement ps = conn.prepareStatement("replace into role_dungeon_total_logs values (?,?,?,?)");
         ps.setLong(1, rid);
         ps.setInt(2, dunId);
         ps.setInt(3, smallId);
         ps.setInt(4, times);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void receive(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         long rid = packet.readLong();
         int dunId = packet.readUnsignedByte();
         int smallId = packet.readUnsignedShort();
         long saveDay = packet.readLong();
         PreparedStatement ps = conn.prepareStatement("update role_dungeon_logs set has_received = 1,base_exp = 0, base_money = 0 where role_id = ? and dun_id = ? and dun_small_id = ? and save_day = ?");
         ps.setLong(1, rid);
         ps.setInt(2, dunId);
         ps.setInt(3, smallId);
         ps.setLong(4, saveDay);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void completeUnReceive(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         long rid = packet.readLong();
         int dunId = packet.readUnsignedByte();
         int smallId = packet.readUnsignedShort();
         long saveDay = packet.readLong();
         long exp = packet.readLong();
         int money = packet.readInt();
         PreparedStatement ps = conn.prepareStatement("update role_dungeon_logs set has_received = 0,base_exp = ?, base_money = ? where role_id = ? and dun_id = ? and dun_small_id = ? and save_day = ?");
         ps.setLong(1, exp);
         ps.setInt(2, money);
         ps.setLong(3, rid);
         ps.setInt(4, dunId);
         ps.setInt(5, smallId);
         ps.setLong(6, saveDay);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var15) {
         var15.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
