package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.unit.player.offline.PlayerDunRecover;
import com.mu.utils.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RecoverDBManager {
   private static final String sqlGetRecover = "select dun_id,recover_times,remainder_times from role_dungeon_recover where role_id = ? and recover_date = ?";
   private static final String sqlGetFinishTimes = "select dun_id,finish_times,save_day from role_dungeon_logs where role_id = ? and dun_id in (1,2,6) and save_day between ? and ? order by save_day desc";
   private static final String sqlGetLastRecover = "select a.dun_id,a.recover_date as recover_day,a.recover_times,a.remainder_times from (select * from role_dungeon_recover where role_id = ? order by recover_date desc) as a group by a.dun_id";
   private static final String sqlInsertRecover = "replace into role_dungeon_recover values (?,?,?,?,?)";

   public static ArrayList getRecoeverList(long rid) {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         long today = Time.getDayLong();
         PreparedStatement ps = conn.prepareStatement("select dun_id,recover_times,remainder_times from role_dungeon_recover where role_id = ? and recover_date = ?");
         ps.setLong(1, rid);
         ps.setLong(2, today);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int dunId = rs.getInt("dun_id");
            int recoverTimes = rs.getInt("recover_times");
            int remainderTimes = rs.getInt("remainder_times");
            PlayerDunRecover recover = new PlayerDunRecover(dunId);
            recover.setRecoverDay(today);
            recover.setRecoverTimes(recoverTimes);
            recover.setRemainderTimes(remainderTimes);
            list.add(recover);
         }

         rs.close();
         ps.close();
      } catch (Exception var15) {
         var15.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static ArrayList getLastRecoverList(long rid) {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select a.dun_id,a.recover_date as recover_day,a.recover_times,a.remainder_times from (select * from role_dungeon_recover where role_id = ? order by recover_date desc) as a group by a.dun_id");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int id = rs.getInt("dun_id");
            long day = rs.getLong("recover_day");
            int recoverTimes = rs.getInt("recover_times");
            int remainderTimes = rs.getInt("remainder_times");
            PlayerDunRecover recover = new PlayerDunRecover(id);
            recover.setRecoverDay(day);
            recover.setRecoverTimes(recoverTimes);
            recover.setRemainderTimes(remainderTimes);
            list.add(recover);
         }

         rs.close();
         ps.close();
      } catch (Exception var15) {
         var15.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static ArrayList getfinishTimes(long rid, long begin, long end) {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select dun_id,finish_times,save_day from role_dungeon_logs where role_id = ? and dun_id in (1,2,6) and save_day between ? and ? order by save_day desc");
         ps.setLong(1, rid);
         ps.setLong(2, begin);
         ps.setLong(3, end);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int dunId = rs.getInt("dun_id");
            int times = rs.getInt("finish_times");
            long saveDay = rs.getLong("save_day");
            list.add(new long[]{(long)dunId, (long)times, saveDay});
         }

         rs.close();
         ps.close();
      } catch (Exception var17) {
         var17.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void saveRecover(long rid, int dunId, int recoverTimes, int remainderTimes, long day) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into role_dungeon_recover values (?,?,?,?,?)");
         ps.setLong(1, rid);
         ps.setInt(2, dunId);
         ps.setLong(3, day);
         ps.setInt(4, recoverTimes);
         ps.setInt(5, remainderTimes);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
