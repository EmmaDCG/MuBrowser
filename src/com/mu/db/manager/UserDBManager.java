package com.mu.db.manager;

import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.game.model.unit.player.User;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.Time;
import com.mu.utils.buffer.BufferReader;
import com.mu.utils.buffer.BufferWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDBManager {
   private static final String Sql_RegistUser = "{ call register_user(?,?,?,?,?,?,?)}";
   private static final String sql_SaveUser = "update mu_user set last_login_time = ?,last_logout_time = ?,online_time = ?,rest_time = ?,user_setup = ?,vip_tag = ? where user_name = ? and server_id = ?";
   private static final String sql_Userfcm = "select need_anti_addiction from mu_user where user_name = ? and server_id = ?";

   public static boolean isFcm(String userName, int serverId) {
      Connection conn = Pool.getConnection();
      boolean isFcm = true;

      try {
         PreparedStatement ps = conn.prepareStatement("select need_anti_addiction from mu_user where user_name = ? and server_id = ?");
         ps.setString(1, userName);
         ps.setInt(2, serverId);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            isFcm = rs.getInt(1) == 1;
         }

         rs.close();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return isFcm;
   }

   public static final byte[] registerUser(String userName, int serverID, boolean needAntiAddiction) {
      BufferWriter writer = new BufferWriter();
      byte[] bytes = null;
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         CallableStatement cs = conn.prepareCall("{ call register_user(?,?,?,?,?,?,?)}");
         long now = System.currentTimeMillis();
         cs.setString(1, userName);
         cs.setInt(2, Global.getPlatID());
         cs.setString(3, Time.getTimeStr(now));
         cs.setInt(4, serverID);
         cs.setLong(5, now);
         cs.setInt(6, needAntiAddiction ? 1 : 0);
         cs.registerOutParameter(7, 4);
         cs.execute();
         boolean hasUser = cs.getInt(7) == 1;
         writer.writeUTF(userName);
         writer.writeInt(serverID);
         writer.writeBoolean(needAntiAddiction);
         writer.writeLong(now);
         writer.writeInt(Global.getPlatID());
         writer.writeInt(Global.getServerID());
         if (!hasUser) {
            writer.writeInt(0);
            writer.writeLong(0L);
            writer.writeInt(0);
            writer.writeInt(0);
            writer.writeInt(0);
         } else {
            ResultSet rs = cs.getResultSet();
            if (rs.next()) {
               writer.writeInt(rs.getInt(1));
               writer.writeLong(rs.getLong(2));
               writer.writeInt(rs.getInt(3));
               writer.writeInt(rs.getInt(4));
               byte[] setupBytes = rs.getBytes(5);
               if (setupBytes == null) {
                  writer.writeInt(0);
               } else {
                  writer.writeInt(setupBytes.length);
                  writer.writeBytes(setupBytes);
               }
            }

            rs.close();
         }

         cs.close();
         writer.writeBoolean(true);
         bytes = writer.toByteArray();
      } catch (Exception var15) {
         var15.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
         writer.destroy();
      }

      return bytes;
   }

   public static User getUser(byte[] bytes) {
      User user = null;
      BufferReader reader = new BufferReader(bytes);

      try {
         String name = reader.readUTF();
         int serverId = reader.readInt();
         boolean needAntiAddiction = reader.readBoolean();
         long loginTime = reader.readLong();
         int platId = reader.readInt();
         int warZoneId = reader.readInt();
         int ingot = reader.readInt();
         long logoutTime = reader.readLong();
         int restTime = reader.readInt();
         int onlineTime = reader.readInt();
         int setupLength = reader.readInt();
         byte[] setupBytes = null;
         if (setupLength > 0) {
            setupBytes = new byte[setupLength];
            reader.readBytes(setupBytes);
         }

         long now = System.currentTimeMillis();
         if (!Time.isSameDay(now, logoutTime)) {
            restTime = 0;
            onlineTime = 0;
         } else {
            restTime += (int)((now - logoutTime) / 1000L);
            if (restTime > 18000) {
               restTime = 0;
               onlineTime = 0;
            }
         }

         user = new User(name);
         user.setServerID(serverId);
         user.setNeedAntiAddiction(needAntiAddiction);
         user.setLastLoginTime(loginTime);
         user.setPlatId(platId);
         user.setIngot(ingot);
         user.setLastLogoutTime(logoutTime);
         user.setRestTime(restTime);
         user.setOnlineTime(onlineTime);
         user.resetSetup(setupBytes);
         user.setWarZoneId(warZoneId);
      } catch (Exception var22) {
         var22.printStackTrace();
      } finally {
         reader.destroy();
      }

      return user;
   }

   public static void saveUser(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         String userName = packet.readUTF();
         long loginTime = packet.readLong();
         long logoutTime = packet.readLong();
         int onlimeTime = packet.readInt();
         int restTime = packet.readInt();
         int serverId = packet.readInt();
         String tag = packet.readUTF();
         int setLenth = packet.readInt();
         byte[] setBytes = new byte[setLenth];
         packet.readBytes(setBytes);
         PreparedStatement ps = conn.prepareStatement("update mu_user set last_login_time = ?,last_logout_time = ?,online_time = ?,rest_time = ?,user_setup = ?,vip_tag = ? where user_name = ? and server_id = ?");
         ps.setLong(1, loginTime);
         ps.setLong(2, logoutTime);
         ps.setInt(3, onlimeTime);
         ps.setInt(4, restTime);
         ps.setBytes(5, setBytes);
         ps.setString(6, tag);
         ps.setString(7, userName);
         ps.setInt(8, serverId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var17) {
         var17.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
