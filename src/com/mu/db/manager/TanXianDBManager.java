package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.io.game.packet.imp.tanxian.TanXianInit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TanXianDBManager {
   private static final String SQL_SELECT_INFORM = "select * from role_tanxian where role_id=?";
   private static final String SQL_UPDATE_INFORM = "replace into role_tanxian value (?, ?, ?, ?) ";

   public static TanXianInit initRoleTanXian(long roleId) {
      TanXianInit packet = new TanXianInit(48000, (byte[])null);
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("select * from role_tanxian where role_id=?");
         ps.setLong(1, roleId);
         ResultSet rs = ps.executeQuery();
         boolean has = rs.next();
         packet.writeBoolean(has);
         if (has) {
            packet.writeInt(rs.getInt("level"));
            packet.writeInt(rs.getInt("exp"));
            packet.writeInt(rs.getInt("ingot_count"));
         }

         rs.close();
         Pool.closeStatment(ps);
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return packet;
   }

   public static void update(long roleId, int level, int exp, int count) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_tanxian value (?, ?, ?, ?) ");
         ps.setLong(1, roleId);
         ps.setInt(2, level);
         ps.setInt(3, exp);
         ps.setInt(4, count);
         ps.executeUpdate();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }
}
