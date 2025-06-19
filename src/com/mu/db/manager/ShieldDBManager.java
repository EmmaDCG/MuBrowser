package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.io.game.packet.imp.shield.InitShield;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ShieldDBManager {
   public static final String SQL_SELECT_SHIELD = "select * from role_shield where roleId=?";
   public static final String SQL_REPLACE_SHIELD = "replace into role_shield value (?,?,?,?,?)";

   public static InitShield initRoleShield(long roleId) {
      InitShield packet = new InitShield();
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("select * from role_shield where roleId=?");
         ps.setLong(1, roleId);
         ResultSet rs = ps.executeQuery();
         boolean has = rs.next();
         packet.writeBoolean(has);
         if (has) {
            packet.writeInt(rs.getInt("level"));
            packet.writeInt(rs.getInt("rank"));
            packet.writeInt(rs.getInt("star"));
            packet.writeInt(rs.getInt("rankExp"));
         }

         rs.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

      return packet;
   }

   public static void replaceShield(long roleId, int level, int rank, int star, int rankExp) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_shield value (?,?,?,?,?)");
         ps.setLong(1, roleId);
         ps.setInt(2, level);
         ps.setInt(3, rank);
         ps.setInt(4, star);
         ps.setInt(5, rankExp);
         ps.execute();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }
}
