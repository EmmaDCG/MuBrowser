package com.mu.game.task.schedule;

import com.mu.db.Pool;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class SaveGangContributionTask extends ScheduleTask {
   private static ConcurrentHashMap roleContributionMap = Tools.newConcurrentHashMap2();
   private static ConcurrentHashMap gangContributionMap = Tools.newConcurrentHashMap2();
   private static final String sqlUpdateRoleContribution = "update mu_gang_member set cur_contribution = ?,his_contribution = ? where role_id = ?";
   private static final String sqlUpdateGangContribution = "update mu_gang set contribution = ?,his_contribution = ? where gang_id = ?";

   public SaveGangContributionTask() {
      this.runWhenStop = true;
   }

   public static void addGang(long gangId, long contribution, long hisContribution) {
      gangContributionMap.put(gangId, new long[]{contribution, hisContribution});
   }

   public static void addMember(long rid, int cur, int his) {
      roleContributionMap.put(rid, new int[]{cur, his});
   }

   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 60000L, 60000L);
   }

   public void doLocalTask() {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps;
         Iterator it;
         Entry entry;
         long key;
         if (roleContributionMap.size() > 0) {
            ps = conn.prepareStatement("update mu_gang_member set cur_contribution = ?,his_contribution = ? where role_id = ?");
            it = roleContributionMap.entrySet().iterator();

            while(it.hasNext()) {
               entry = (Entry)it.next();
               key = ((Long)entry.getKey()).longValue();
               int[] value = (int[])entry.getValue();
               ps.setInt(1, value[0]);
               ps.setInt(2, value[1]);
               ps.setLong(3, key);
               ps.addBatch();
               it.remove();
            }

            ps.executeBatch();
            ps.clearBatch();
            ps.close();
         }

         if (gangContributionMap.size() > 0) {
            ps = conn.prepareStatement("update mu_gang set contribution = ?,his_contribution = ? where gang_id = ?");
            it = gangContributionMap.entrySet().iterator();

            while(it.hasNext()) {
               entry = (Entry)it.next();
               key = ((Long)entry.getKey()).longValue();
               long[] value = (long[])entry.getValue();
               ps.setLong(1, value[0]);
               ps.setLong(2, value[1]);
               ps.setLong(3, key);
               ps.addBatch();
               it.remove();
            }

            ps.executeBatch();
            ps.clearBatch();
            ps.close();
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public void doInterTask() {
   }
}
