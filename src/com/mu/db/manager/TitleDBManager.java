package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.unit.player.title.Title;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

public class TitleDBManager {
   private static final String sqlGetTitle = "select title_id,time,isEquip from role_titles where role_id = ?";
   private static final String sqlInsertTitle = "replace into role_titles values (?,?,?,?)";
   private static final String sqlDeleteTitle = "delete from role_titles where role_id = ? and title_id = ?";
   private static final String sqlUpdateEquip = "update role_titles set isEquip = ? where role_id = ? and title_id = ?";
   private static final String sqlClearTitle = "update role_titles set isEquip = 0 where role_id = ? ";

   public static ArrayList getTitleList(long rid) {
      Connection conn = null;
      ArrayList list = new ArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select title_id,time,isEquip from role_titles where role_id = ?");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int id = rs.getInt("title_id");
            long time = rs.getLong("time");
            boolean isEquip = rs.getInt("isEquip") == 1;
            Title title = new Title(id);
            title.setExpiredTime(time);
            title.setEquip(isEquip);
            list.add(title);
         }

         rs.close();
         ps.close();
      } catch (Exception var14) {
         var14.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void insertTitle(long rid, int titleId, long time, boolean isEquip) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into role_titles values (?,?,?,?)");
         ps.setLong(1, rid);
         ps.setInt(2, titleId);
         ps.setLong(3, time);
         ps.setInt(4, isEquip ? 1 : 0);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deleteTitle(long rid, int titleId) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("delete from role_titles where role_id = ? and title_id = ?");
         ps.setLong(1, rid);
         ps.setInt(2, titleId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void updateEquip(long rid, int titleId, boolean isEquip) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update role_titles set isEquip = ? where role_id = ? and title_id = ?");
         ps.setInt(1, isEquip ? 1 : 0);
         ps.setLong(2, rid);
         ps.setInt(3, titleId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void clearTitle(long rid) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update role_titles set isEquip = 0 where role_id = ? ");
         ps.setLong(1, rid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deleteTitle(long rid, ArrayList titleList) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("delete from role_titles where role_id = ? and title_id = ?");
         Iterator var6 = titleList.iterator();

         while(var6.hasNext()) {
            int titleId = ((Integer)var6.next()).intValue();
            ps.setLong(1, rid);
            ps.setInt(2, titleId);
            ps.addBatch();
         }

         ps.executeBatch();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
