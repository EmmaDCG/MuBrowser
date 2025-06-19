package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class StorageDBManager {
   private static String sqlGetStrorage = "SELECT container_type,grid_count FROM mu_storage WHERE role_id = ?";
   private static String sqlUpdateStorage = "REPLACE INTO mu_storage(role_id ,container_type,grid_count) VALUES(?,?,?)";

   public static HashMap initStorage(long roleId) {
      Connection conn = Pool.getConnection();
      HashMap storageMap = new HashMap();

      try {
         PreparedStatement ps = conn.prepareStatement(sqlGetStrorage);
         ps.setLong(1, roleId);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int storageType = rs.getInt("container_type");
            int gridCount = rs.getInt("grid_count");
            storageMap.put(storageType, gridCount);
         }

         rs.close();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return storageMap;
   }

   public static void updateStorage(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         long roleId = (long)packet.readDouble();
         int containerType = packet.readByte();
         int gridCount = packet.readShort();
         PreparedStatement ps = conn.prepareStatement(sqlUpdateStorage);
         ps.setLong(1, roleId);
         ps.setInt(2, containerType);
         ps.setInt(3, gridCount);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
