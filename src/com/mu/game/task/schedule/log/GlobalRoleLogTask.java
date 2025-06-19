package com.mu.game.task.schedule.log;

import com.mu.db.Pool;
import com.mu.db.log.global.GlobalRoleLog;
import com.mu.game.task.schedule.ScheduleTask;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalRoleLogTask extends ScheduleTask {
   private static ConcurrentHashMap map = Tools.newConcurrentHashMap2();
   private static final String sqlUpdateRole = "insert into mu_role values (?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE role_level = ?,profession = ?,profession_level = ?";

   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 120000L, 120000L);
   }

   public GlobalRoleLogTask() {
      this.runWhenStop = true;
   }

   public void doLocalTask() {
      Connection conn = null;
      String timeNow = Time.getTimeStr();

      try {
         conn = Pool.getGlobalLogConnection();
         PreparedStatement ps = conn.prepareStatement("insert into mu_role values (?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE role_level = ?,profession = ?,profession_level = ?");
         Iterator it = map.values().iterator();

         while(it.hasNext()) {
            GlobalRoleLog log = (GlobalRoleLog)it.next();
            ps.setLong(1, log.getRid());
            ps.setString(2, log.getUserName());
            ps.setString(3, log.getName());
            ps.setInt(4, log.getServerId());
            ps.setString(5, timeNow);
            ps.setInt(6, log.getLevel());
            ps.setInt(7, log.getProfession());
            ps.setInt(8, log.getProfessionLevel());
            ps.setInt(9, log.getBindIngot());
            ps.setInt(10, log.getLevel());
            ps.setInt(11, log.getProfession());
            ps.setInt(12, log.getProfessionLevel());
            ps.addBatch();
            it.remove();
         }

         ps.executeBatch();
         ps.clearBatch();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void addRoleLog(GlobalRoleLog log) {
      map.put(log.getRid(), log);
   }

   public void doInterTask() {
      this.doLocalTask();
   }
}
