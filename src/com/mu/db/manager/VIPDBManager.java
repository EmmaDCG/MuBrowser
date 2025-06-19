package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.io.game.packet.imp.vip.InitVIP;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VIPDBManager {
   private static final String SQL_SELECT_VIP = "select * from role_vip where roleId=? ";
   private static final String SQL_REPLACE_VIP = "replace into role_vip value (?, ?, ?, ?, ?) ";

   public static InitVIP initRoleVIP(long roleId) {
      InitVIP iv = new InitVIP();
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("select * from role_vip where roleId=? ");
         ps.setLong(1, roleId);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            iv.writeByte(rs.getInt("vipId"));
            iv.writeLong(rs.getLong("activeTime"));
            iv.writeShort(rs.getInt("buyDays"));
            iv.writeShort(rs.getInt("expDays"));
         }

         rs.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

      return iv;
   }

   public static void replaceVIP(long roleId, int vipId, long activeTime, int buyDays, int expDays) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_vip value (?, ?, ?, ?, ?) ");
         ps.setLong(1, roleId);
         ps.setInt(2, vipId);
         ps.setLong(3, activeTime);
         ps.setInt(4, buyDays);
         ps.setInt(5, expDays);
         ps.execute();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }
}
