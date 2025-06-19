package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.unit.skill.model.SkillDBEntry;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SkillDBManager {
   private static final String sqlLearnSkill = "REPLACE INTO mu_skills (role_id,skill_id,skill_level,passive_consume) VALUES(?,?,?,?)";
   private static final String sqlUpdateSelected = "UPDATE mu_skills SET selected = ? WHERE role_id = ? AND skill_id = ?";
   private static final String sqlSaveOffLine = "UPDATE mu_skills SET remain_thaw_time = ?  WHERE role_id = ? AND skill_id = ?";
   private static final String sqlSearchSkills = "SELECT * FROM mu_skills WHERE role_id = ?";
   private static final String sqlUpdatePassiveConsume = "UPDATE mu_skills SET passive_consume = ? WHERE role_id = ? AND skill_id = ?";

   public static void updateSkill(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         PreparedStatement ps = null;
         long roleID = packet.readLong();
         int skillID = packet.readInt();
         int type = packet.readByte();
         switch(type) {
         case 1:
            int level = packet.readByte();
            ps = conn.prepareStatement("REPLACE INTO mu_skills (role_id,skill_id,skill_level,passive_consume) VALUES(?,?,?,?)");
            ps.setLong(1, roleID);
            ps.setInt(2, skillID);
            ps.setInt(3, level);
            ps.setInt(4, packet.readInt());
            break;
         case 2:
            boolean selected = packet.readBoolean();
            ps = conn.prepareStatement("UPDATE mu_skills SET selected = ? WHERE role_id = ? AND skill_id = ?");
            ps.setBoolean(1, selected);
            ps.setLong(2, roleID);
            ps.setInt(3, skillID);
            break;
         case 3:
            int passiveConsume = packet.readInt();
            ps = conn.prepareStatement("UPDATE mu_skills SET passive_consume = ? WHERE role_id = ? AND skill_id = ?");
            ps.setInt(1, passiveConsume);
            ps.setLong(2, roleID);
            ps.setInt(3, skillID);
         }

         if (ps != null) {
            ps.executeUpdate();
            ps.close();
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static List searchSkills(long roleID) {
      Connection conn = Pool.getConnection();
      ArrayList entries = new ArrayList();

      try {
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM mu_skills WHERE role_id = ?");
         ps.setLong(1, roleID);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int skillID = rs.getInt("skill_id");
            int level = rs.getInt("skill_level");
            int remainThawTime = rs.getInt("remain_thaw_time");
            boolean selected = rs.getBoolean("selected");
            int passiveConsume = rs.getInt("passive_consume");
            SkillDBEntry entry = new SkillDBEntry();
            entry.setSkillID(skillID);
            entry.setLevel(level);
            entry.setRemainThawTime(remainThawTime);
            entry.setSelected(selected);
            entry.setPassiveConsume(passiveConsume);
            entries.add(entry);
         }

         rs.close();
         ps.close();
      } catch (Exception var15) {
         var15.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return entries;
   }

   public static void saveWhenOffLine(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();
      PreparedStatement ps = null;

      try {
         long roleID = packet.readLong();
         int size = packet.readByte();
         ps = conn.prepareStatement("UPDATE mu_skills SET remain_thaw_time = ?  WHERE role_id = ? AND skill_id = ?");

         for(int i = 0; i < size; ++i) {
            int skillID = packet.readInt();
            int remainTime = packet.readInt();
            ps.setInt(1, remainTime);
            ps.setLong(2, roleID);
            ps.setInt(3, skillID);
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
}
