package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.unit.player.shortcut.ShortcutEntry;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShortcutDBManager {
   private static final String sqlDelShortcut = "DELETE FROM mu_shortcut WHERE role_id = ? AND sc_position = ?";
   private static final String sqlUpdateShortcut = "REPLACE INTO mu_shortcut(role_id,sc_position,sc_type,model_id) VALUES(?,?,?,?)";
   private static final String sqlSearchShortcut = "SELECT sc_position,sc_type,model_id FROM mu_shortcut WHERE role_id = ?";

   public static void saveShortcut(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         PreparedStatement delPs = null;
         PreparedStatement updatePs = null;
         long roleID = packet.readLong();
         int delSize = packet.readByte();
         if (delSize > 0) {
            delPs = conn.prepareStatement("DELETE FROM mu_shortcut WHERE role_id = ? AND sc_position = ?");

            for(int i = 0; i < delSize; ++i) {
               delPs.setLong(1, roleID);
               delPs.setInt(2, packet.readByte());
               delPs.addBatch();
            }

            delPs.executeBatch();
         }

         int updateSize = packet.readByte();
         if (updateSize > 0) {
            updatePs = conn.prepareStatement("REPLACE INTO mu_shortcut(role_id,sc_position,sc_type,model_id) VALUES(?,?,?,?)");

            for(int i = 0; i < updateSize; ++i) {
               updatePs.setLong(1, roleID);
               updatePs.setInt(2, packet.readByte());
               updatePs.setInt(3, packet.readByte());
               updatePs.setInt(4, packet.readInt());
               updatePs.addBatch();
            }

            updatePs.executeBatch();
         }

         if (delPs != null) {
            delPs.close();
         }

         if (updatePs != null) {
            updatePs.close();
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static List searchShortcut(long roleID) {
      Connection conn = Pool.getConnection();
      ArrayList entries = new ArrayList();

      try {
         PreparedStatement ps = conn.prepareStatement("SELECT sc_position,sc_type,model_id FROM mu_shortcut WHERE role_id = ?");
         ps.setLong(1, roleID);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int position = rs.getInt("sc_position");
            int type = rs.getInt("sc_type");
            int modelID = rs.getInt("model_id");
            ShortcutEntry entry = new ShortcutEntry(type, modelID, position);
            entries.add(entry);
         }

         rs.close();
         ps.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return entries;
   }
}
