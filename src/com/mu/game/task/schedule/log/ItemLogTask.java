package com.mu.game.task.schedule.log;

import com.mu.db.Pool;
import com.mu.db.log.atom.ItemForgingLogAtom;
import com.mu.db.log.atom.ItemLogAtom;
import com.mu.game.task.schedule.ScheduleTask;
import com.mu.io.game.packet.imp.equip.RequestForgingEquip;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ItemLogTask extends ScheduleTask {
   private static ConcurrentLinkedQueue logLink = new ConcurrentLinkedQueue();
   private static ConcurrentLinkedQueue forgingLink = new ConcurrentLinkedQueue();
   private static String insertItemLog = "INSERT INTO mu_item_log(role_id,nick_name,TYPE,item_id,model_id,item_name,count,quality,star_level,zhuijia_level,socket,bind,expire_time,durability,stats,stones,runes,source,log_time,type_name) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   private static String insertItemForgingLog = "INSERT INTO mu_item_forging_log(role_id,nick_name,item_id,model_id,item_name,forging_type,type_name,quality,pre_operation_level,new_operation_level,stats,stones,runes,log_time,STATUS,reduce_ingot,reduce_money) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

   public ItemLogTask() {
      this.runWhenStop = true;
   }

   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 60000L, 120000L);
   }

   public void doLocalTask() {
      if (logLink.size() > 0) {
         ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
            public void run() {
               ItemLogTask.saveLog();
            }
         });
      }

      if (forgingLink.size() > 0) {
         ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
            public void run() {
               ItemLogTask.saveForging();
            }
         });
      }

   }

   private static void saveLog() {
      if (logLink.size() >= 1) {
         Connection conn = null;

         try {
            conn = Pool.getLogConnection();
            PreparedStatement ps = conn.prepareStatement(insertItemLog);
            int count = 0;

            while(!logLink.isEmpty()) {
               ItemLogAtom iLog = (ItemLogAtom)logLink.poll();
               ps.setLong(1, iLog.getRoleID());
               ps.setString(2, iLog.getNickName());
               ps.setInt(3, iLog.getType());
               ps.setLong(4, iLog.getItemId());
               ps.setInt(5, iLog.getModelID());
               ps.setString(6, iLog.getItemName());
               ps.setInt(7, iLog.getCount());
               ps.setInt(8, iLog.getQuality());
               ps.setInt(9, iLog.getStarLevel());
               ps.setInt(10, iLog.getZhuijiaLevel());
               ps.setInt(11, iLog.getSocket());
               ps.setBoolean(12, iLog.isBind());
               ps.setLong(13, iLog.getExpireTime());
               ps.setInt(14, iLog.getDurability());
               ps.setString(15, iLog.getStatStr());
               ps.setString(16, iLog.getStones());
               ps.setString(17, iLog.getRunes());
               ps.setInt(18, iLog.getSource());
               ps.setString(19, iLog.getLogTime());
               ps.setString(20, iLog.getType() == 1 ? "添加" : "删除");
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
         } catch (Exception var7) {
            var7.printStackTrace();
         } finally {
            Pool.closeConnection(conn);
         }

      }
   }

   private static void saveForging() {
      if (forgingLink.size() >= 1) {
         Connection conn = null;

         try {
            conn = Pool.getLogConnection();
            PreparedStatement ps = conn.prepareStatement(insertItemForgingLog);
            int count = 0;

            while(!forgingLink.isEmpty()) {
               ItemForgingLogAtom iLog = (ItemForgingLogAtom)forgingLink.poll();
               ps.setLong(1, iLog.getRoleID());
               ps.setString(2, iLog.getNickname());
               ps.setLong(3, iLog.getItemID());
               ps.setInt(4, iLog.getModelID());
               ps.setString(5, iLog.getItemName());
               ps.setInt(6, iLog.getType());
               ps.setString(7, RequestForgingEquip.getForgingName(iLog.getType()));
               ps.setInt(8, iLog.getQuality());
               ps.setInt(9, iLog.getPreOperationLevel());
               ps.setInt(10, iLog.getNewOperationLevel());
               ps.setString(11, iLog.getStats());
               ps.setString(12, iLog.getStones());
               ps.setString(13, iLog.getRunes());
               ps.setString(14, iLog.getLogTime());
               ps.setInt(15, iLog.getStatus());
               ps.setInt(16, iLog.getReduceIngot());
               ps.setInt(17, iLog.getReduceMoney());
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
         } catch (Exception var7) {
            var7.printStackTrace();
         } finally {
            Pool.closeConnection(conn);
         }

      }
   }

   public static void addItemLog(Game2GatewayPacket packet) throws Exception {
      ItemLogAtom iLog = ItemLogAtom.createItemLog(packet);
      if (iLog != null) {
         logLink.add(iLog);
      }

   }

   public static void addItemForgingLog(Game2GatewayPacket packet) throws Exception {
      ItemForgingLogAtom fLog = ItemForgingLogAtom.createItemLog(packet);
      if (fLog != null) {
         forgingLink.add(fLog);
      }

   }

   public void doInterTask() {
   }
}
