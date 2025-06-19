package com.mu.db.manager;

import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.game.IDFactory;
import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.top.WorldLevelInfo;
import com.mu.game.model.top.WorldLevelManager;
import com.mu.game.model.unit.monster.worldboss.KillBossRecord;
import com.mu.game.model.unit.monster.worldboss.WorldBossData;
import com.mu.game.model.unit.monster.worldboss.WorldBossManager;
import com.mu.game.top.TopManager;
import com.mu.utils.RndNames;
import com.mu.utils.Time;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameDBManager {
   public static final String getBeUsedNames = "select used_name from be_used_names";
   public static final String getRepeatNames = "select used_name,count(used_name) as num from be_used_names group by used_name having num >1";
   public static final String deleteNames = "delete from be_used_names where used_name = ?";
   public static final String insertNames = "insert into be_used_names values (?)";
   public static final String getMaxID = "{call mu_max_id(?,?,?,?,?,?)}";
   public static final String SQL_QUERY_BAN = "select * from mu_ban";
   public static final String sqlInterMaxID = "select id_type, max(id_value) from mu_max_id group by (id_type)";
   public static final String sqlInsertInterMaxID = "insert into mu_max_id values (?,?) ON DUPLICATE KEY UPDATE id_value = if(id_value < ?,?,id_value)";
   public static final String sqlGetKillBossRecord = "select * from mu_kill_boss_record k where 5>(select count(*) from mu_kill_boss_record where k.boss_id=boss_id and kill_time>k.kill_time)";
   public static final String getWorldLevel = "select role_id,role_name,role_level from mu_role order by role_level desc,levelup_time limit 30";
   private static final String saveClinetInfo = "insert into mu_client_info values (?,?,?,?,?,?)";
   private static final String saveMapOnline = "insert into mu_map_online_log values (?,?,?,?,?)";
   private static final String getNewUser = "select count(1) from mu_user where register_date > ?";
   private static final Logger logger = LoggerFactory.getLogger(GameDBManager.class);

   public static boolean initGameDataFromDB() {
      Connection conn = Pool.getConnection();

      try {
         initRndName(conn);
         initMuMaxIds(conn);
         initMuBan(conn);
         initKillBossRecord(conn);
         if (Global.isInterServiceServer()) {
            dealInterServer(conn);
         }

         GangDBManager.initGangs();
         TopManager.init();
         ActivityDBManager.initServerLimit();
         return true;
      } catch (Exception var5) {
         var5.printStackTrace();
         logger.error("init game data failed");
      } finally {
         Pool.closeConnection(conn);
      }

      return false;
   }

   public static int getNewUser(String date) {
      Connection conn = null;
      int num = 0;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select count(1) from mu_user where register_date > ?");
         ps.setString(1, date);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            num = rs.getInt(1);
         }

         rs.close();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return num;
   }

   private static void initMuBan(Connection conn) throws Exception {
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("select * from mu_ban");

      while(rs.next()) {
         int type = rs.getInt("type");
         String mark = rs.getString("mark");
         String timestamp = rs.getString("timestamp");
         Global.addMuBan(type, mark, timestamp);
      }

      rs.close();
      st.close();
   }

   private static void initMuMaxIds(Connection conn) throws Exception {
      if (!Global.isInterServiceServer()) {
         CallableStatement cs = conn.prepareCall("{call mu_max_id(?,?,?,?,?,?)}");
         cs.registerOutParameter(1, -5);
         cs.registerOutParameter(2, -5);
         cs.registerOutParameter(3, -5);
         cs.registerOutParameter(4, -5);
         cs.registerOutParameter(5, -5);
         cs.registerOutParameter(6, -5);
         cs.execute();
         long rid = cs.getLong(1);
         long itemID = cs.getLong(2);
         long gangID = cs.getLong(3);
         long mailID = cs.getLong(4);
         long marketID = cs.getLong(5);
         long redPacketId = cs.getLong(6);
         itemID /= 100000000L;
         long tmpItemID = marketID / 100000000L;
         if (itemID < tmpItemID) {
            itemID = tmpItemID;
         }

         IDFactory.setInitialRoleID(rid / 100000000L);
         IDFactory.setInitialItemID(itemID);
         IDFactory.setInitialGangID(gangID / 100000000L);
         IDFactory.setInitialMailID(mailID / 100000000L);
         IDFactory.setInitialRedPacketID(redPacketId / 100000000L);
      }
   }

   private static void initInterMaxIds(Connection conn) throws Exception {
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("select id_type, max(id_value) from mu_max_id group by (id_type)");

      while(rs.next()) {
         int type = rs.getInt(1);
         long value = rs.getLong(2);
         switch(type) {
         case 1:
            IDFactory.setInitialItemID(value / 100000000L);
            break;
         case 2:
            IDFactory.setInitialMailID(value / 100000000L);
         }
      }

      rs.close();
      st.close();
   }

   private static void deleteRepeatNames(Connection conn) throws Exception {
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("select used_name,count(used_name) as num from be_used_names group by used_name having num >1");
      HashSet repeatNameSet = new HashSet();

      while(rs.next()) {
         repeatNameSet.add(rs.getString("used_name"));
      }

      rs.close();
      st.close();
      int repeatSize = repeatNameSet.size();
      if (repeatSize > 0) {
         PreparedStatement psDeleteNames = conn.prepareStatement("delete from be_used_names where used_name = ?");
         int i = 1;

         for(Iterator var8 = repeatNameSet.iterator(); var8.hasNext(); ++i) {
            String name = (String)var8.next();
            psDeleteNames.setString(1, name);
            psDeleteNames.addBatch();
            if (i % 1000 == 0 || i == repeatSize) {
               psDeleteNames.executeBatch();
            }
         }

         psDeleteNames.close();
         i = 1;
         PreparedStatement psInsert = conn.prepareStatement("insert into be_used_names values (?)");

         for(Iterator var9 = repeatNameSet.iterator(); var9.hasNext(); ++i) {
            String name = (String)var9.next();
            psInsert.setString(1, name);
            psInsert.addBatch();
            if (i % 1000 == 0 || i == repeatSize) {
               psInsert.executeBatch();
            }
         }

         psInsert.close();
      }

   }

   private static void initBeUsedName(Connection conn) throws Exception {
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("select used_name from be_used_names");

      while(rs.next()) {
         RndNames.addUsedName(rs.getString("used_name"));
      }

      rs.close();
      st.close();
   }

   private static void initRndName(Connection conn) throws Exception {
      deleteRepeatNames(conn);
      initBeUsedName(conn);
      RndNames.createNames();
   }

   private static void dealInterServer(Connection conn) throws Exception {
      initInterMaxIds(conn);
   }

   public static void insertInterMaxId(int type, long value) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("insert into mu_max_id values (?,?) ON DUPLICATE KEY UPDATE id_value = if(id_value < ?,?,id_value)");
         ps.setInt(1, type);
         ps.setLong(2, value);
         ps.setLong(3, value);
         ps.setLong(4, value);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   private static void initKillBossRecord(Connection conn) throws Exception {
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("select * from mu_kill_boss_record k where 5>(select count(*) from mu_kill_boss_record where k.boss_id=boss_id and kill_time>k.kill_time)");

      while(rs.next()) {
         int id = rs.getInt("boss_id");
         String name = rs.getString("killer_name");
         long time = rs.getLong("kill_time");
         WorldBossData data = WorldBossManager.getBossData(id);
         if (data != null) {
            KillBossRecord kr = new KillBossRecord();
            kr.setBossId(id);
            kr.setKillerName(name);
            kr.setKillTime(time);
            data.addRecord(kr);
         }
      }

      rs.close();
      st.close();
   }

   public static CopyOnWriteArrayList getWorldLevel() {
      Connection conn = null;
      CopyOnWriteArrayList list = new CopyOnWriteArrayList();

      try {
         conn = Pool.getConnection();
         WorldLevelManager.clearInfo();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("select role_id,role_name,role_level from mu_role order by role_level desc,levelup_time limit 30");

         while(rs.next()) {
            long rid = rs.getLong("role_id");
            String name = rs.getString("role_name");
            int level = rs.getInt("role_level");
            WorldLevelInfo info = new WorldLevelInfo();
            info.setRid(rid);
            info.setName(name);
            info.setLevel(level);
            list.add(info);
         }

         rs.close();
         st.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void saveMapOnlie() {
      Connection conn = null;

      try {
         ArrayList list = MapConfig.getMapOnlineSize();
         if (list.size() == 0) {
            return;
         }

         String date = Time.getTimeStr();
         conn = Pool.getLogConnection();
         PreparedStatement ps = conn.prepareStatement("insert into mu_map_online_log values (?,?,?,?,?)");
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            Map map = (Map)var5.next();
            ps.setInt(1, map.getID());
            ps.setString(2, map.getName());
            ps.setInt(3, map.getLine() + 1);
            ps.setInt(4, map.getTruePlayerSize());
            ps.setString(5, date);
            ps.addBatch();
         }

         ps.executeBatch();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveClientInfo(long rid, String s1, String s2, String s3, String s4, String s5) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("insert into mu_client_info values (?,?,?,?,?,?)");
         ps.setLong(1, rid);
         ps.setString(2, s1);
         ps.setString(3, s2);
         ps.setString(4, s3);
         ps.setString(5, s4);
         ps.setString(6, s5);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
