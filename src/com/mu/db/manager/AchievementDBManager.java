package com.mu.db.manager;

import com.mu.db.Pool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

public class AchievementDBManager {
   private static final String saveAchievement = "replace into  role_achievement values (?,?,?)";
   private static final String getAchievement = "select * from role_achievement where role_id = ?";

   public static void saveAchievement(long rid, ArrayList list) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into  role_achievement values (?,?,?)");
         Iterator var6 = list.iterator();

         while(var6.hasNext()) {
            int[] in = (int[])var6.next();
            ps.setLong(1, rid);
            ps.setInt(2, in[0]);
            ps.setInt(3, in[1]);
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

   public static ArrayList getAchievement(long rid) {
      Connection conn = null;
      ArrayList list = new ArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select * from role_achievement where role_id = ?");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int id = rs.getInt("arc_id");
            int num = rs.getInt("arc_num");
            list.add(new int[]{id, num});
         }

         rs.close();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }
}
