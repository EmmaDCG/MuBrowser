package com.mu.db.log;

import com.mu.db.Pool;
import com.mu.utils.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class LogDBManager {
   private static String sqlAddInogt = "INSERT INTO mu_add_ingot_log(user_name ,role_id,role_name ,ingot,add_type,add_time,role_server_id,server_id,type_name) VALUES(?,?,?,?,?,?,?,?,?)";
   private static String sqlBindAddInogt = "INSERT INTO mu_add_bindingot_log(user_name ,role_id,role_name ,bind_ingot,add_type,add_time,role_server_id,server_id,type_name) VALUES(?,?,?,?,?,?,?,?,?)";

   public static void addIngotLog(String userName, long roleID, String roleName, int ingot, int addType, int roleServerID, int serverID) {
      Connection conn = Pool.getGlobalLogConnection();

      try {
         PreparedStatement ps = conn.prepareStatement(sqlAddInogt);
         ps.setString(1, userName);
         ps.setLong(2, roleID);
         ps.setString(3, roleName);
         ps.setInt(4, ingot);
         ps.setInt(5, addType);
         ps.setString(6, Time.getTimeStr());
         ps.setInt(7, roleServerID);
         ps.setInt(8, serverID);
         ps.setString(9, IngotChangeType.getTypeName(addType));
         ps.executeUpdate();
         ps.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void addBindIngotLog(String userName, long roleID, String roleName, int bindIngot, int addType, int roleServerID, int serverID) {
      Connection conn = Pool.getGlobalLogConnection();

      try {
         PreparedStatement ps = conn.prepareStatement(sqlBindAddInogt);
         ps.setString(1, userName);
         ps.setLong(2, roleID);
         ps.setString(3, roleName);
         ps.setInt(4, bindIngot);
         ps.setInt(5, addType);
         ps.setString(6, Time.getTimeStr());
         ps.setInt(7, roleServerID);
         ps.setInt(8, serverID);
         ps.setString(9, IngotChangeType.getTypeName(addType));
         ps.executeUpdate();
         ps.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
