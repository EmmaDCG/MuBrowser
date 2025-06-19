package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.io.game.packet.imp.financing.InitFinancing;
import com.mu.utils.buffer.BufferWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FinancingDBManager {
   private static final String SQL_SELECT_ITEM = "select * from role_financing_item where roleId=? ";
   private static final String SQL_SELECT_REWARD = "select * from role_financing_reward where roleId=? ";
   private static final String SQL_REPLACE_ITEM = "replace into role_financing_item value (?, ?, ?) ";
   private static final String SQL_REPLACE_REWARD = "replace into role_financing_reward value (?, ?, ?) ";

   public static InitFinancing initRoleFinancing(long roleId) {
      InitFinancing packet = new InitFinancing();
      Connection conn = null;
      PreparedStatement ps = null;
      BufferWriter bw = new BufferWriter();

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("select * from role_financing_item where roleId=? ");
         ps.setLong(1, roleId);
         ResultSet rs = ps.executeQuery();
         int xsIndex = bw.getWriteLength();
         int itemCount = 0;
         bw.writeByte(itemCount);

         while(rs.next()) {
            bw.writeByte(rs.getInt("itemId"));
            bw.writeByte(rs.getInt("loginDay"));
            ++itemCount;
         }

         bw.getWriteBuf()[xsIndex] = (byte)itemCount;
         rs.close();
         Pool.closeStatment(ps);
         ps = conn.prepareStatement("select * from role_financing_reward where roleId=? ");
         ps.setLong(1, roleId);
         rs = ps.executeQuery();

         while(rs.next()) {
            bw.writeByte(rs.getInt("rewardId"));
            bw.writeLong(rs.getLong("receiveTime"));
         }

         rs.close();
         Pool.closeStatment(ps);
         packet.writeBytes(bw.toByteArray());
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return packet;
   }

   public static void replaceItem(long roleId, int itemId, int loginDay) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_financing_item value (?, ?, ?) ");
         ps.setLong(1, roleId);
         ps.setInt(2, itemId);
         ps.setInt(3, loginDay);
         ps.execute();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void replaceReward(long roleId, int itemId, long receiveTime) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_financing_reward value (?, ?, ?) ");
         ps.setLong(1, roleId);
         ps.setInt(2, itemId);
         ps.setLong(3, receiveTime);
         ps.execute();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }
}
