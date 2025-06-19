package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.unit.player.hang.HangDBEntry;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HangDBManager {
   private static final String sqlUpdateHangset = "REPLACE INTO role_hangset(role_id,cycles,pickup_others,qualities,hang_skills,hang_sales) VALUES(?,?,?,?,?,?)";
   private static final String sqlSearchHangset = "SELECT * FROM role_hangset WHERE role_id = ?";

   public static void updateHangset(Game2GatewayPacket packet) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         long roleID = packet.readLong();
         String cycles = packet.readUTF();
         String pickupOthers = packet.readUTF();
         String qualities = packet.readUTF();
         String hangSkills = packet.readUTF();
         String hangSales = packet.readUTF();
         conn = Pool.getConnection();
         ps = conn.prepareStatement("REPLACE INTO role_hangset(role_id,cycles,pickup_others,qualities,hang_skills,hang_sales) VALUES(?,?,?,?,?,?)");
         ps.setLong(1, roleID);
         ps.setString(2, cycles);
         ps.setString(3, pickupOthers);
         ps.setString(4, qualities);
         ps.setString(5, hangSkills);
         ps.setString(6, hangSales);
         ps.executeUpdate();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static HangDBEntry searchHangset(long roleID) {
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      HangDBEntry entry = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("SELECT * FROM role_hangset WHERE role_id = ?");
         ps.setLong(1, roleID);
         rs = ps.executeQuery();
         if (rs.next()) {
            String cycles = rs.getString("cycles");
            String pickupOthers = rs.getString("pickup_others");
            String qualities = rs.getString("qualities");
            String hang_skills = rs.getString("hang_skills");
            String hangSales = rs.getString("hang_sales");
            entry = new HangDBEntry(roleID, cycles, pickupOthers, qualities, hang_skills, hangSales);
         }
      } catch (Exception var14) {
         var14.printStackTrace();
      } finally {
         Pool.closeResultSet(rs);
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

      return entry;
   }
}
