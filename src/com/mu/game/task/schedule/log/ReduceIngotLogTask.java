package com.mu.game.task.schedule.log;

import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.db.log.IngotChangeType;
import com.mu.db.log.atom.IngotChangeLogAtom;
import com.mu.game.task.schedule.ScheduleTask;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ReduceIngotLogTask extends ScheduleTask {
   private static ConcurrentLinkedQueue logLink = new ConcurrentLinkedQueue();
   private static ConcurrentLinkedQueue bindIngotLogLink = new ConcurrentLinkedQueue();
   private static String sqlLogStr = "INSERT INTO mu_reduce_ingot_log(role_id,role_name,user_name,ingot,reduce_type,TIME,role_server_id,server_id,type_name,reduce_detail) VALUES(?,?,?,?,?,?,?,?,?,?)";
   private static String sqlBindIngotLogStr = "INSERT INTO mu_reduce_bindingot_log(role_id,role_name,user_name,bind_ingot,reduce_type,TIME,role_server_id,server_id,type_name,reduce_detail) VALUES(?,?,?,?,?,?,?,?,?,?)";
   public static final int intervalTime = 60000;

   public ReduceIngotLogTask() {
      this.runWhenStop = true;
   }

   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 60000L, 60000L);
   }

   public void doLocalTask() {
      if (logLink.size() > 0) {
         ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
            public void run() {
               ReduceIngotLogTask.saveLog();
            }
         });
      }

      if (bindIngotLogLink.size() > 0) {
         ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
            public void run() {
               ReduceIngotLogTask.saveBindIngotLog();
            }
         });
      }

   }

   private static void saveLog() {
      if (logLink.size() >= 1) {
         Connection conn = null;

         try {
            conn = Pool.getGlobalLogConnection();
            PreparedStatement ps = conn.prepareStatement(sqlLogStr);
            int count = 0;
            int serverId = Global.getServerID();

            while(!logLink.isEmpty()) {
               IngotChangeLogAtom logAtom = (IngotChangeLogAtom)logLink.poll();
               ps.setLong(1, logAtom.getRoleID());
               ps.setString(2, logAtom.getRoleName());
               ps.setString(3, logAtom.getUserName());
               ps.setInt(4, logAtom.getIngot());
               ps.setInt(5, logAtom.getChangeType());
               ps.setString(6, logAtom.getChangeTime());
               ps.setInt(7, logAtom.getRoleServerID());
               ps.setInt(8, serverId);
               ps.setString(9, IngotChangeType.getTypeName(logAtom.getChangeType()));
               ps.setString(10, logAtom.getChangeDetail());
               ps.addBatch();
               ++count;
               if (count % 1000 == 0) {
                  ps.executeBatch();
                  ps.clearBatch();
               }
            }

            ps.executeBatch();
            ps.clearBatch();
            ps.close();
         } catch (Exception var8) {
            var8.printStackTrace();
         } finally {
            Pool.closeConnection(conn);
         }

      }
   }

   private static void saveBindIngotLog() {
      if (bindIngotLogLink.size() >= 1) {
         Connection conn = null;

         try {
            conn = Pool.getGlobalLogConnection();
            PreparedStatement ps = conn.prepareStatement(sqlBindIngotLogStr);
            int count = 0;
            int serverId = Global.getServerID();

            while(!bindIngotLogLink.isEmpty()) {
               IngotChangeLogAtom logAtom = (IngotChangeLogAtom)bindIngotLogLink.poll();
               ps.setLong(1, logAtom.getRoleID());
               ps.setString(2, logAtom.getRoleName());
               ps.setString(3, logAtom.getUserName());
               ps.setInt(4, logAtom.getIngot());
               ps.setInt(5, logAtom.getChangeType());
               ps.setString(6, logAtom.getChangeTime());
               ps.setInt(7, logAtom.getRoleServerID());
               ps.setInt(8, serverId);
               ps.setString(9, IngotChangeType.getTypeName(logAtom.getChangeType()));
               ps.setString(10, logAtom.getChangeDetail());
               ps.addBatch();
               ++count;
               if (count % 1000 == 0) {
                  ps.executeBatch();
                  ps.clearBatch();
               }
            }

            ps.executeBatch();
            ps.clearBatch();
            ps.close();
         } catch (Exception var8) {
            var8.printStackTrace();
         } finally {
            Pool.closeConnection(conn);
         }

      }
   }

   public static void addChangeLog(Game2GatewayPacket packet) throws Exception {
      IngotChangeLogAtom logAtom = IngotChangeLogAtom.createLogAtom(packet);
      if (logAtom != null) {
         logLink.add(logAtom);
      }

   }

   public static void addBindIngotLog(Game2GatewayPacket packet) throws Exception {
      IngotChangeLogAtom logAtom = IngotChangeLogAtom.createLogAtom(packet);
      if (logAtom != null) {
         bindIngotLogLink.add(logAtom);
      }

   }

   public void doInterTask() {
      this.doLocalTask();
   }
}
