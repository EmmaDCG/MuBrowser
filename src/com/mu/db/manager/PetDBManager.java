package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.io.game.packet.imp.pet.InitPet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PetDBManager {
   public static final String SQL_SELECT_PET = "select * from role_pet where roleId=? ";
   public static final String SQL_SELECT_ATTRIBUTE = "select * from role_pet_attribute where roleId=? ";
   public static final String SQL_REPLACE_PET = "replace into role_pet value (?,?,?,?,? ,?,?,?) ";
   public static final String SQL_REPLACE_ATTRIBUTE = "replace into role_pet_attribute value (?,?,?,?) ";

   public static InitPet initRolePet(long roleId) {
      InitPet packet = new InitPet();
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("select * from role_pet where roleId=? ");
         ps.setLong(1, roleId);
         ResultSet rs = ps.executeQuery();
         boolean hasPet = rs.next();
         packet.writeBoolean(hasPet);
         if (hasPet) {
            packet.writeByte(rs.getInt("rank"));
            packet.writeShort(rs.getInt("level"));
            packet.writeLong(rs.getLong("exp"));
            packet.writeInt(rs.getInt("luck"));
            packet.writeBoolean(rs.getBoolean("show"));
            packet.writeLong(rs.getLong("diedTime"));
            packet.writeLong(rs.getLong("rankTime"));
         }

         rs.close();
         Pool.closeStatment(ps);
         ps = conn.prepareStatement("select * from role_pet_attribute where roleId=? ");
         ps.setLong(1, roleId);
         rs = ps.executeQuery();
         int index = packet.getWriteLength();
         int count = 0;
         packet.writeByte(count);

         while(rs.next()) {
            packet.writeShort(rs.getInt("statId"));
            packet.writeInt(rs.getInt("level"));
            packet.writeInt(rs.getInt("value"));
            ++count;
         }

         packet.getWriteBuf()[index] = (byte)count;
         rs.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

      return packet;
   }

   public static void replacePet(long roleId, int rank, int level, long exp, int luck, boolean show, long diedTime, long rankTime) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_pet value (?,?,?,?,? ,?,?,?) ");
         ps.setLong(1, roleId);
         ps.setInt(2, rank);
         ps.setInt(3, level);
         ps.setLong(4, exp);
         ps.setInt(5, luck);
         ps.setBoolean(6, show);
         ps.setLong(7, diedTime);
         ps.setLong(8, rankTime);
         ps.execute();
      } catch (Exception var18) {
         var18.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void replaceAttribute(long roleId, int statId, int level, int value) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_pet_attribute value (?,?,?,?) ");
         ps.setLong(1, roleId);
         ps.setInt(2, statId);
         ps.setInt(3, level);
         ps.setInt(4, value);
         ps.execute();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }
}
