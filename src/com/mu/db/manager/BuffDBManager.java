package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.unit.buff.model.BuffDBEntry;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BuffDBManager {
   private static final String sqlUpdateBuff = "REPLACE INTO mu_buffs(role_id,buff_id,buff_level,remain_time,prop_str,quit_time) VALUES(?,?,?,?,?,?)";
   private static final String sqlDeleteBuff = "DELETE FROM mu_buffs WHERE role_id = ? AND buff_id = ?";
   private static final String sqlSearchBuff = "SELECT * FROM mu_buffs WHERE role_id = ? ";

   public static List searchBuffs(long roleID) {
      Connection conn = Pool.getConnection();
      List entries = new ArrayList();
      List delBuffs = null;
      PreparedStatement ps = null;
      PreparedStatement delPs = null;

      try {
         ps = conn.prepareStatement("SELECT * FROM mu_buffs WHERE role_id = ? ");
         ps.setLong(1, roleID);
         ResultSet rs = ps.executeQuery();
         long now = System.currentTimeMillis();

         while(true) {
            while(rs.next()) {
               int buffModelID = rs.getInt("buff_id");
               int level = rs.getInt("buff_level");
               long remainTime = (long)rs.getInt("remain_time");
               String propStr = rs.getString("prop_str");
               long quit_time = rs.getLong("quit_time");
               BuffModel model = BuffModel.getModel(buffModelID);
               if (model == null) {
                  if (delBuffs == null) {
                     delBuffs = new ArrayList();
                  }

                  delBuffs.add(buffModelID);
               } else {
                  if (model.isCalOfflineTime()) {
                     remainTime -= now - quit_time;
                     if (remainTime <= 0L) {
                        if (delBuffs == null) {
                           delBuffs = new ArrayList();
                        }

                        delBuffs.add(buffModelID);
                        continue;
                     }
                  }

                  BuffDBEntry entry = new BuffDBEntry();
                  entry.setBuffModelID(buffModelID);
                  entry.setLevel(level);
                  entry.setDuration(remainTime);
                  entry.setPropStr(propStr);
                  entries.add(entry);
               }
            }

            rs.close();
            if (delBuffs != null && delBuffs.size() > 0) {
               delPs = conn.prepareStatement("DELETE FROM mu_buffs WHERE role_id = ? AND buff_id = ?");
               Iterator var25 = delBuffs.iterator();

               while(var25.hasNext()) {
                  Integer buffID = (Integer)var25.next();
                  delPs.setLong(1, roleID);
                  delPs.setInt(2, buffID.intValue());
                  delPs.addBatch();
               }

               delPs.executeBatch();
               delBuffs.clear();
            }
            break;
         }
      } catch (Exception var22) {
         var22.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeStatment(delPs);
         Pool.closeConnection(conn);
      }

      return entries;
   }

   public static void saveWhenOffLine(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();
      PreparedStatement upPs = null;
      PreparedStatement delPs = null;

      try {
         long roleID = packet.readLong();
         int size = packet.readByte();
         long now = System.currentTimeMillis();

         for(int i = 0; i < size; ++i) {
            int buffModelID = packet.readInt();
            int buffLevel = packet.readShort();
            long remainTime = packet.readLong();
            String propStr = packet.readUTF();
            if (remainTime == 0L) {
               if (delPs == null) {
                  delPs = conn.prepareStatement("DELETE FROM mu_buffs WHERE role_id = ? AND buff_id = ?");
               }

               delPs.setLong(1, roleID);
               delPs.setInt(2, buffModelID);
               delPs.addBatch();
            } else {
               if (upPs == null) {
                  upPs = conn.prepareStatement("REPLACE INTO mu_buffs(role_id,buff_id,buff_level,remain_time,prop_str,quit_time) VALUES(?,?,?,?,?,?)");
               }

               upPs.setLong(1, roleID);
               upPs.setInt(2, buffModelID);
               upPs.setInt(3, buffLevel);
               upPs.setLong(4, remainTime);
               upPs.setString(5, propStr);
               upPs.setLong(6, now);
               upPs.addBatch();
            }
         }

         if (delPs != null) {
            delPs.executeBatch();
         }

         if (upPs != null) {
            upPs.executeBatch();
         }
      } catch (Exception var18) {
         var18.printStackTrace();
      } finally {
         Pool.closeStatment(delPs);
         Pool.closeStatment(upPs);
         Pool.closeConnection(conn);
      }

   }

   public static void delBuff(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();
      PreparedStatement delPs = null;

      try {
         long roleID = packet.readLong();
         int buffID = packet.readInt();
         delPs = conn.prepareStatement("DELETE FROM mu_buffs WHERE role_id = ? AND buff_id = ?");
         delPs.setLong(1, roleID);
         delPs.setInt(2, buffID);
         delPs.execute();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeStatment(delPs);
         Pool.closeConnection(conn);
      }

   }
}
