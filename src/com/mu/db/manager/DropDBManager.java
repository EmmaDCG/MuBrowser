package com.mu.db.manager;

import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class DropDBManager {
   private static final String sqlAddFirstKill = "REPLACE INTO role_kill_number(role_id,template_id,number,first_day) VALUES(?,?,?,?)";
   private static final String sqlAddKillNumber = "UPDATE role_kill_number SET number = ? WHERE role_id = ? AND template_id = ?";
   private static final String sqlRoleDropCounts = "REPLACE INTO role_drop_counts(role_id,drop_id,drop_count,drop_day) VALUES(?,?,?,?)";
   private static final String sqlServerDropCounts = "REPLACE INTO mu_server_drop_counts(server_id,drop_id,drop_count,drop_day) VALUES(?,?,?,?)";
   private static final String sqlSearchKillNumber = "SELECT template_id,number FROM role_kill_number WHERE role_id = ?";
   private static final String sqlSearchRoleDropCounts = "SELECT drop_id,drop_count,drop_day FROM role_drop_counts WHERE role_id = ?";

   public static void addFirstKill(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();
      PreparedStatement ps = null;

      try {
         long roleID = packet.readLong();
         int templateID = packet.readInt();
         int killNumber = packet.readInt();
         long killday = Time.getDayLong();
         ps = conn.prepareStatement("REPLACE INTO role_kill_number(role_id,template_id,number,first_day) VALUES(?,?,?,?)");
         ps.setLong(1, roleID);
         ps.setInt(2, templateID);
         ps.setInt(3, killNumber);
         ps.setLong(4, killday);
         ps.executeUpdate();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void saveKillNumber(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();
      PreparedStatement ps = null;

      try {
         ps = conn.prepareStatement("UPDATE role_kill_number SET number = ? WHERE role_id = ? AND template_id = ?");
         long roleID = packet.readLong();
         int size = packet.readShort();

         for(int i = 1; i <= size; ++i) {
            int templateID = packet.readInt();
            int number = packet.readInt();
            ps.setInt(1, number);
            ps.setLong(2, roleID);
            ps.setInt(3, templateID);
            ps.addBatch();
         }

         ps.executeBatch();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void saveRoleDropMap(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();
      PreparedStatement ps = null;

      try {
         ps = conn.prepareStatement("REPLACE INTO role_drop_counts(role_id,drop_id,drop_count,drop_day) VALUES(?,?,?,?)");
         long day = Time.getDayLong();
         long roleID = packet.readLong();
         int size = packet.readShort();

         for(int i = 1; i <= size; ++i) {
            ps.setLong(1, roleID);
            ps.setInt(2, packet.readInt());
            ps.setInt(3, packet.readInt());
            ps.setLong(4, day);
            ps.addBatch();
         }

         ps.executeBatch();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void saveServerDropMap(HashMap dropCounts) {
      if (dropCounts != null && dropCounts.size() >= 1) {
         Connection conn = Pool.getConnection();
         PreparedStatement ps = null;

         try {
            ps = conn.prepareStatement("REPLACE INTO mu_server_drop_counts(server_id,drop_id,drop_count,drop_day) VALUES(?,?,?,?)");
            long day = Time.getDayLong();
            int serverID = Global.getServerID();
            Iterator it = dropCounts.entrySet().iterator();

            while(it.hasNext()) {
               Entry entry = (Entry)it.next();
               int dropID = ((Integer)entry.getKey()).intValue();
               int dropCount = ((Integer)entry.getValue()).intValue();
               ps.setLong(1, (long)serverID);
               ps.setInt(2, dropID);
               ps.setInt(3, dropCount);
               ps.setLong(4, day);
               ps.addBatch();
            }

            ps.executeBatch();
         } catch (Exception var13) {
            var13.printStackTrace();
         } finally {
            Pool.closeStatment(ps);
            Pool.closeConnection(conn);
         }

      }
   }

   public static HashMap searchKillNumber(long roleID) {
      Connection conn = Pool.getConnection();
      PreparedStatement ps = null;
      HashMap fkMap = null;

      try {
         ps = conn.prepareStatement("SELECT template_id,number FROM role_kill_number WHERE role_id = ?");
         ps.setLong(1, roleID);

         ResultSet rs;
         int templateID;
         int killNumber;
         for(rs = ps.executeQuery(); rs.next(); fkMap.put(templateID, killNumber)) {
            templateID = rs.getInt(1);
            killNumber = rs.getInt(2);
            if (fkMap == null) {
               fkMap = new HashMap();
            }
         }

         rs.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

      return fkMap;
   }

   public static HashMap searchRoleDropCounts(long roleID) {
      Connection conn = Pool.getConnection();
      PreparedStatement ps = null;
      HashMap dropCounts = null;

      try {
         ps = conn.prepareStatement("SELECT drop_id,drop_count,drop_day FROM role_drop_counts WHERE role_id = ?");
         ps.setLong(1, roleID);
         long today = Time.getDayLong();
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int dropId = rs.getInt("drop_id");
            int count = rs.getInt("drop_count");
            long day = rs.getLong("drop_day");
            if (day == today) {
               if (dropCounts == null) {
                  dropCounts = new HashMap();
               }

               dropCounts.put(dropId, count);
            }
         }

         rs.close();
      } catch (Exception var15) {
         var15.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

      return dropCounts;
   }
}
