package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.io.game.packet.imp.task.InitTask;
import com.mu.utils.buffer.BufferWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TaskDBManager {
   private static final String SQL_SELECT_INFORM = "select * from role_task_inform where roleId=?";
   private static final String SQL_SELECT_TASK = "select * from role_task where roleId=?";
   private static final String SQL_SELECT_XS_COUNT = "select * from role_task_xs where roleId=?";
   private static final String SQL_INSERT_DELETE = "{ call task_insert_delete(?,?,?,?,?) }";
   private static final String SQL_INSERT_TASK = "replace into role_task value (?, ?, ?, ?, ?) ";
   private static final String SQL_DELETE_TASK = "delete from role_task where roleId = ? and taskId = ? ";
   private static final String SQL_DELETE_TASK_CLAZZ = "delete from role_task where roleId = ? and type = ? ";
   private static final String SQL_UPDATE_TASK = "update role_task set state =?, rateStr =? where roleId=? and taskId=? ";
   private static final String SQL_REPLACE_INFORM = "replace into role_task_inform value (?, ?, ?, ?) ";
   private static final String SQL_REPLACE_XS_COUNT = "replace into role_task_xs value (?, ?, ?) ";
   private static final String SQL_CLEAR_XS_COUNT = "delete from role_task_xs where roleId = ? ";

   public static InitTask initRoleTask(long roleId) {
      InitTask it = new InitTask();
      Connection conn = null;
      PreparedStatement ps = null;
      BufferWriter bw = new BufferWriter();

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("select * from role_task_inform where roleId=?");
         ps.setLong(1, roleId);
         ResultSet rs = ps.executeQuery();
         boolean has = rs.next();
         bw.writeBoolean(has);
         if (has) {
            bw.writeInt(rs.getInt("curRCStar"));
            bw.writeInt(rs.getInt("curRCRemain"));
            bw.writeInt(rs.getInt("curRCBuy"));
         }

         rs.close();
         Pool.closeStatment(ps);
         ps = conn.prepareStatement("select * from role_task_xs where roleId=?");
         ps.setLong(1, roleId);
         rs = ps.executeQuery();
         int xsIndex = bw.getWriteLength();
         int xsCount = 0;
         bw.writeByte(xsCount);

         while(rs.next()) {
            bw.writeInt(rs.getInt("xsId"));
            bw.writeInt(rs.getInt("count"));
            ++xsCount;
         }

         bw.getWriteBuf()[xsIndex] = (byte)xsCount;
         rs.close();
         Pool.closeStatment(ps);
         ps = conn.prepareStatement("select * from role_task where roleId=?");
         ps.setLong(1, roleId);
         rs = ps.executeQuery();

         while(rs.next()) {
            bw.writeInt(rs.getInt("taskId"));
            bw.writeByte(rs.getInt("state"));
            bw.writeUTF(rs.getString("rateStr"));
         }

         rs.close();
         Pool.closeStatment(ps);
         it.writeBytes(bw.toByteArray());
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return it;
   }

   public static void insertAndDelete(long roleId, int taskId, int clazz, int state, String rateStr) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("{ call task_insert_delete(?,?,?,?,?) }");
         ps.setLong(1, roleId);
         ps.setInt(2, taskId);
         ps.setInt(3, clazz);
         ps.setInt(4, state);
         ps.setString(5, rateStr);
         ps.execute();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void insert(long roleId, int taskId, int clazz, int state, String rateStr) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_task value (?, ?, ?, ?, ?) ");
         ps.setLong(1, roleId);
         ps.setInt(2, taskId);
         ps.setInt(3, clazz);
         ps.setInt(4, state);
         ps.setString(5, rateStr);
         ps.executeUpdate();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void delete(long roleId, int taskId) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("delete from role_task where roleId = ? and taskId = ? ");
         ps.setLong(1, roleId);
         ps.setInt(2, taskId);
         ps.executeUpdate();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void deleteClazz(long roleId, int clazz) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("delete from role_task where roleId = ? and type = ? ");
         ps.setLong(1, roleId);
         ps.setInt(2, clazz);
         ps.executeUpdate();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void update(long roleId, int taskId, int state, String rateStr) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("update role_task set state =?, rateStr =? where roleId=? and taskId=? ");
         ps.setInt(1, state);
         ps.setString(2, rateStr);
         ps.setLong(3, roleId);
         ps.setInt(4, taskId);
         ps.executeUpdate();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void replaceXSCount(long roleId, int xsId, int count) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_task_xs value (?, ?, ?) ");
         ps.setLong(1, roleId);
         ps.setInt(2, xsId);
         ps.setInt(3, count);
         ps.executeUpdate();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void clearXSCount(long roleId) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("delete from role_task_xs where roleId = ? ");
         ps.setLong(1, roleId);
         ps.executeUpdate();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }

   public static void replaceInform(long roleId, int curRCStar, int curRCRemain, int sumRCBuy) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("replace into role_task_inform value (?, ?, ?, ?) ");
         ps.setLong(1, roleId);
         ps.setInt(2, curRCStar);
         ps.setInt(3, curRCRemain);
         ps.setInt(4, sumRCBuy);
         ps.executeUpdate();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

   }
}
