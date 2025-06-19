package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.friend.Friend;
import com.mu.game.model.friend.FriendBlessInfo;
import com.mu.game.model.unit.player.Profession;
import com.mu.utils.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FriendDBManager {
   private static final String getFriend = "select a.*,b.profession,b.profession_level,b.zdl,b.role_level,b.logout_time, c.vip_tag from mu_friends a,mu_role b,mu_user c where a.role_id = ? and a.friend_id = b.role_id and a.friend_server_id = b.server_id and c.user_name = b.user_name and c.server_id = b.server_id";
   private static final String addFriend = "replace into mu_friends values (?,?,?,?,?,?,?,?)";
   private static final String delAllRelationship = "delete from mu_friends where role_id = ? and friend_id = ?";
   private static final String delFriend = "delete from mu_friends where role_id = ? and friend_id = ? and friend_type = ?";
   private static final String getRoleInfo = "select role_name,role_level,server_id from mu_role where role_id = ?";
   private static final String updateFriend = "update mu_friends set friend_degree = ?,bekilled_time = ? where role_id = ? and friend_id = ? and friend_type = ?";
   private static final String updateBless = "replace into mu_bless values (?,?,?)";
   private static final String getTodayBlessTime = "select bless_times from mu_bless where role_id = ? and save_day = ?";
   private static final String getTodayWishInfo = "select * from mu_wish where role_id = ? ";
   private static final String updateLucky = " insert into mu_wish (role_id,cur_lucky,update_day) values (?,?,?) ON DUPLICATE KEY UPDATE cur_lucky = ?,update_day = ?";
   private static final String updateReceiveDay = " insert into mu_wish (role_id,receive_day) values (?,?) ON DUPLICATE KEY UPDATE receive_day = ?";
   private static final String saveBlessRecord = "replace into mu_bless_record values(?,?,?,?,?)";
   private static final String getBlessList = "select * from mu_bless_record where friend_id = ? and bless_day = ? limit 100";

   public static void addFriend(long rid, long fid, int type, int fsid, String fname, int killTimes) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into mu_friends values (?,?,?,?,?,?,?,?)");
         ps.setLong(1, rid);
         ps.setLong(2, fid);
         ps.setInt(3, type);
         ps.setInt(4, fsid);
         ps.setString(5, fname);
         ps.setLong(6, System.currentTimeMillis());
         ps.setInt(7, 0);
         ps.setInt(8, killTimes);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void updateRoleBless(long rid, int times, long day) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into mu_bless values (?,?,?)");
         ps.setLong(1, rid);
         ps.setInt(2, times);
         ps.setLong(3, day);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static int getRoleBlessTimes(long rid) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select bless_times from mu_bless where role_id = ? and save_day = ?");
         ps.setLong(1, rid);
         ps.setLong(2, Time.getDayLong());
         ResultSet rs = ps.executeQuery();
         int count = 0;
         if (rs.next()) {
            count = rs.getInt("bless_times");
         }

         rs.close();
         ps.close();
         int var7 = count;
         return var7;
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return 0;
   }

   public static long[] getRoleWishInfo(long rid) {
      Connection conn = null;
      long[] l = new long[3];

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select * from mu_wish where role_id = ? ");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            int lucky = rs.getInt("cur_lucky");
            long updateTime = rs.getLong("update_day");
            long receiveDay = rs.getLong("receive_day");
            l[0] = (long)lucky;
            l[1] = updateTime;
            l[2] = receiveDay;
         }

         rs.close();
         ps.close();
      } catch (Exception var14) {
         var14.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return l;
   }

   public static Friend doOfflineAddBlack(long ownerId, long blackId) {
      Connection conn = null;
      Friend f = null;

      try {
         String name = null;
         int serverId = 0;
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select role_name,role_level,server_id from mu_role where role_id = ?");
         ps.setLong(1, blackId);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            name = rs.getString("role_name");
            serverId = rs.getInt("server_id");
         }

         rs.close();
         ps.close();
         if (name != null) {
            addFriend(ownerId, blackId, 2, serverId, name, 0);
            f = new Friend(blackId, 2);
            Friend var11 = f;
            return var11;
         }
      } catch (Exception var14) {
         var14.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return null;
   }

   public static void updateLucky(long rid, int lucky, long saveDay) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement(" insert into mu_wish (role_id,cur_lucky,update_day) values (?,?,?) ON DUPLICATE KEY UPDATE cur_lucky = ?,update_day = ?");
         ps.setLong(1, rid);
         ps.setInt(2, lucky);
         ps.setLong(3, saveDay);
         ps.setInt(4, lucky);
         ps.setLong(5, saveDay);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void updateReceievDay(long rid, int lucky, long saveDay) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement(" insert into mu_wish (role_id,receive_day) values (?,?) ON DUPLICATE KEY UPDATE receive_day = ?");
         ps.setLong(1, rid);
         ps.setLong(2, saveDay);
         ps.setLong(3, saveDay);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void updateFriend(long roleId, long fid, int type, int degree, int killTimes) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_friends set friend_degree = ?,bekilled_time = ? where role_id = ? and friend_id = ? and friend_type = ?");
         ps.setInt(1, degree);
         ps.setInt(2, killTimes);
         ps.setLong(3, roleId);
         ps.setLong(4, fid);
         ps.setInt(5, type);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static ArrayList getFriendList(long rid) {
      Connection conn = null;
      ArrayList list = new ArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select a.*,b.profession,b.profession_level,b.zdl,b.role_level,b.logout_time, c.vip_tag from mu_friends a,mu_role b,mu_user c where a.role_id = ? and a.friend_id = b.role_id and a.friend_server_id = b.server_id and c.user_name = b.user_name and c.server_id = b.server_id");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long fid = rs.getLong("friend_id");
            String name = rs.getString("friend_name");
            int type = rs.getInt("friend_type");
            int sid = rs.getInt("friend_server_id");
            long addTime = rs.getLong("add_time");
            int degree = rs.getInt("friend_degree");
            int beKilledTimes = rs.getInt("bekilled_time");
            int pro = rs.getInt("profession");
            int proLevel = rs.getInt("profession_level");
            int zdl = rs.getInt("zdl");
            String vipTag = rs.getString("vip_tag");
            int roleLevel = rs.getInt("role_level");
            long offlineTime = rs.getLong("logout_time");
            Friend f = new Friend(fid, type);
            f.setAddTime(addTime);
            f.setBeKilledTimes(beKilledTimes);
            f.setBlueTag(vipTag);
            f.setFriendDegree(degree);
            f.setLevel(roleLevel);
            f.setName(name);
            f.setOfflineTime(offlineTime);
            f.setProfession(Profession.getProID(pro, proLevel));
            f.setServerId(sid);
            f.setZdl(zdl);
            list.add(f);
         }

         rs.close();
         ps.close();
      } catch (Exception var26) {
         var26.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void delAllRelationship(long rid, long fid) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("delete from mu_friends where role_id = ? and friend_id = ?");
         ps.setLong(1, rid);
         ps.setLong(2, fid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static ArrayList getBlessList(long rid) {
      Connection conn = null;
      ArrayList list = new ArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select * from mu_bless_record where friend_id = ? and bless_day = ? limit 100");
         ps.setLong(1, rid);
         ps.setLong(2, Time.getDayLong());
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long roleId = rs.getLong("role_id");
            String name = rs.getString("role_name");
            long time = rs.getLong("bless_time");
            FriendBlessInfo bi = new FriendBlessInfo();
            bi.setId(roleId);
            bi.setName(name);
            bi.setTime(time);
            list.add(bi);
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

   public static void saveRecord(long rid, String name, long fid, long day, long time) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into mu_bless_record values(?,?,?,?,?)");
         ps.setLong(1, rid);
         ps.setLong(2, fid);
         ps.setString(3, name);
         ps.setLong(4, day);
         ps.setLong(5, time);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var14) {
         var14.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void delFriend(long rid, long fid, int type) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("delete from mu_friends where role_id = ? and friend_id = ? and friend_type = ?");
         ps.setLong(1, rid);
         ps.setLong(2, fid);
         ps.setInt(3, type);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
