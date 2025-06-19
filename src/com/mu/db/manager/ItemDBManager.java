package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemSaveAide;
import com.mu.game.model.item.ItemTools;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemDBManager {
   private static final Logger logger = LoggerFactory.getLogger(ItemDBManager.class);
   private static final String sqlGetAllItem = "SELECT * FROM mu_items WHERE role_id = ?";
   private static final String sqlGetItemByContainerType = "SELECT * FROM mu_items WHERE role_id = ? and container_type = ?";
   private static final String sqlUpdateItemCount = "UPDATE mu_items SET COUNT = ? WHERE item_id = ?";
   private static final String sqlUpdateItemBind = "UPDATE mu_items SET bind = ? WHERE item_id = ?";
   private static final String sqlUpdateItemSlot = "UPDATE mu_items SET slot = ? WHERE item_id = ?";
   private static final String sqlUpgradeItem = "UPDATE mu_items SET model_id = ?,SOCKET = ? WHERE item_id = ?";
   private static final String sqlUpdateContainer = "UPDATE mu_items SET container_type = ?, slot = ?,count = ? WHERE item_id = ?";
   private static final String sqlUpdateSlotAndCount = "UPDATE mu_items SET slot = ?,count = ? WHERE item_id = ?";
   private static final String sqlUpdateStarLevel = "UPDATE mu_items SET star_level = ?, once_max_star_level = ?, star_up_times = ?, bind = ?,runes = ? WHERE item_id = ?";
   private static final String sqlUpdateBasisStat = "UPDATE mu_items SET basis_stats = ? WHERE item_id = ?";
   private static final String sqlUpdateOtherStat = "UPDATE mu_items SET other_stats = ? WHERE item_id = ?";
   private static final String sqlUpdateStats = "UPDATE mu_items SET basis_stats = ? ,other_stats = ? WHERE item_id = ?";
   private static final String sqlUpdateStones = "UPDATE mu_items SET stones = ? WHERE item_id = ?";
   private static final String sqlUpdateRunes = "UPDATE mu_items SET runes = ?,bind = ? WHERE item_id = ?";
   private static final String sqlUpdateDurability = "UPDATE mu_items SET durability = ? WHERE item_id = ?";
   private static final String sqlUpdateZhuijia = "UPDATE mu_items SET zhuijia_level = ? WHERE item_id = ?";
   private static final String sqlUpdateToShop = "UPDATE mu_items SET container_type = ?,slot = ?,money = ? ,money_type = ? WHERE item_id = ?";
   private static final String sqlAddItem = "INSERT INTO mu_items(item_id,role_id,model_id,quality,COUNT,slot,container_type,star_level,SOCKET,bind,money,money_type,star_up_times,once_max_star_level,expire_time,durability,basis_stats,other_stats,stones,runes,zhuijia_level)  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   private static final String sqlDeleteItem = "DELETE FROM mu_items WHERE item_id = ?";
   private static final String sqlUpdateStorage = "REPLACE INTO mu_storage(role_id ,container_type,grid_count,cooled_count,cooled_time) VALUES(?,?,?,?,?)";
   private static final String sqlSearchStorage = "SELECT * FROM mu_storage WHERE role_id = ?";
   private static final String sqlSearchItemLimit = "SELECT limit_model_id,limit_count FROM role_item_limits WHERE role_id = ? AND limit_day = ? ";
   private static final String sqlUpdateItemLimits = "REPLACE INTO role_item_limits(role_id,limit_model_id,limit_day,limit_count) VALUES(?,?,?,?)";

   public static ArrayList initItemList(long roleId) {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         String sql = "SELECT * FROM mu_items WHERE role_id = ?";
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setLong(1, roleId);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            ItemSaveAide isa = initISA(rs);
            list.add(isa);
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

   public static HashMap getItemByType(long roleID, int type) {
      HashMap itemMap = new HashMap();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         String sql = "SELECT * FROM mu_items WHERE role_id = ? and container_type = ?";
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setLong(1, roleID);
         ps.setInt(2, type);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            ItemSaveAide isa = initISA(rs);
            Item item = ItemTools.loadItem(isa);
            if (item != null) {
               itemMap.put(item.getID(), item);
            }
         }

         rs.close();
         ps.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return itemMap;
   }

   public static ItemSaveAide initISA(ResultSet rs) throws Exception {
      long itemId = rs.getLong("item_id");
      int modelID = rs.getInt("model_id");
      int quality = rs.getInt("quality");
      int count = rs.getInt("count");
      int slot = rs.getInt("slot");
      int containerType = rs.getInt("container_type");
      int starLevel = rs.getInt("star_level");
      int socket = rs.getInt("socket");
      boolean bind = rs.getBoolean("bind");
      int money = rs.getInt("money");
      int moneyType = rs.getInt("money_type");
      int starUpTimes = rs.getInt("star_up_times");
      int onceMaxStarLevel = rs.getInt("once_max_star_level");
      long expireTime = rs.getLong("expire_time");
      int durability = rs.getInt("durability");
      String basisStats = rs.getString("basis_stats");
      String otherStats = rs.getString("other_stats");
      String stones = rs.getString("stones");
      String runes = rs.getString("runes");
      int zhuijiaLevel = rs.getInt("zhuijia_level");
      ItemSaveAide isa = new ItemSaveAide(itemId, modelID, quality, count, slot, containerType, starLevel, socket, bind, money, moneyType, starUpTimes, onceMaxStarLevel, expireTime, durability, basisStats, otherStats, stones, runes, zhuijiaLevel);
      return isa;
   }

   public static int insertItem(long rid, Item item) {
      int result = 1;
      Connection conn = Pool.getConnection();

      try {
         PreparedStatement ps = conn.prepareStatement("INSERT INTO mu_items(item_id,role_id,model_id,quality,COUNT,slot,container_type,star_level,SOCKET,bind,money,money_type,star_up_times,once_max_star_level,expire_time,durability,basis_stats,other_stats,stones,runes,zhuijia_level)  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
         insertDetial(rid, item, ps);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
         result = 2;
      } finally {
         Pool.closeConnection(conn);
      }

      return result;
   }

   public static void insertDetial(long roleID, Item item, PreparedStatement ps) throws Exception {
      long itemId = item.getID();
      int modelID = item.getModelID();
      int quality = item.getQuality();
      int count = item.getCount();
      int slot = item.getSlot();
      int containerType = item.getContainerType();
      int starLevel = item.getStarLevel();
      int socket = item.getSocket();
      boolean bind = item.isBind();
      int money = item.getMoney();
      int moneyType = item.getMoneyType();
      int starUpTimes = item.getStarUpTimes();
      int onceMaxStarLevel = item.getOnceMaxStarLevel();
      long expireTime = item.getExpireTime();
      int durability = item.getDurability();
      String basisStats = item.getBasisStr();
      String otherStats = item.getOtherStr();
      String stones = item.getStoneStr();
      String runes = item.getRuneStr();
      int zhuijiaLevel = item.getZhuijiaLevel();
      ps.setLong(1, itemId);
      ps.setLong(2, roleID);
      ps.setInt(3, modelID);
      ps.setInt(4, quality);
      ps.setInt(5, count);
      ps.setInt(6, slot);
      ps.setInt(7, containerType);
      ps.setInt(8, starLevel);
      ps.setInt(9, socket);
      ps.setBoolean(10, bind);
      ps.setInt(11, money);
      ps.setInt(12, moneyType);
      ps.setInt(13, starUpTimes);
      ps.setInt(14, onceMaxStarLevel);
      ps.setLong(15, expireTime);
      ps.setInt(16, durability);
      ps.setString(17, basisStats);
      ps.setString(18, otherStats);
      ps.setString(19, stones);
      ps.setString(20, runes);
      ps.setInt(21, zhuijiaLevel);
   }

   public static int insertItem(Game2GatewayPacket packet) {
      int result = 1;
      Connection conn = Pool.getConnection();

      try {
         long rid = packet.readLong();
         long itemId = packet.readLong();
         int modelID = packet.readInt();
         int quality = packet.readByte();
         int count = packet.readInt();
         int slot = packet.readShort();
         int containerType = packet.readByte();
         int starLevel = packet.readByte();
         int socket = packet.readByte();
         boolean bind = packet.readBoolean();
         int money = packet.readInt();
         int moneyType = packet.readByte();
         int starUpTimes = packet.readInt();
         int onceMaxStarLevel = packet.readByte();
         long expireTime = packet.readLong();
         int durability = packet.readShort();
         String basisStats = packet.readUTF();
         String otherStats = packet.readUTF();
         String stones = packet.readUTF();
         String runes = packet.readUTF();
         int zhuijiaLevel = packet.readByte();
         PreparedStatement ps = conn.prepareStatement("INSERT INTO mu_items(item_id,role_id,model_id,quality,COUNT,slot,container_type,star_level,SOCKET,bind,money,money_type,star_up_times,once_max_star_level,expire_time,durability,basis_stats,other_stats,stones,runes,zhuijia_level)  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
         ps.setLong(1, itemId);
         ps.setLong(2, rid);
         ps.setInt(3, modelID);
         ps.setInt(4, quality);
         ps.setInt(5, count);
         ps.setInt(6, slot);
         ps.setInt(7, containerType);
         ps.setInt(8, starLevel);
         ps.setInt(9, socket);
         ps.setBoolean(10, bind);
         ps.setInt(11, money);
         ps.setInt(12, moneyType);
         ps.setInt(13, starUpTimes);
         ps.setInt(14, onceMaxStarLevel);
         ps.setLong(15, expireTime);
         ps.setInt(16, durability);
         ps.setString(17, basisStats);
         ps.setString(18, otherStats);
         ps.setString(19, stones);
         ps.setString(20, runes);
         ps.setInt(21, zhuijiaLevel);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var31) {
         var31.printStackTrace();
         result = 2;
      } finally {
         Pool.closeConnection(conn);
      }

      return result;
   }

   public static void updateItem(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         long itemId = packet.readLong();
         int type = packet.readByte();
         PreparedStatement ps = null;
         switch(type) {
         case 1:
            ps = conn.prepareStatement("UPDATE mu_items SET COUNT = ? WHERE item_id = ?");
            ps.setInt(1, packet.readInt());
            ps.setLong(2, itemId);
            break;
         case 2:
            ps = conn.prepareStatement("UPDATE mu_items SET slot = ? WHERE item_id = ?");
            ps.setInt(1, packet.readShort());
            ps.setLong(2, itemId);
            break;
         case 3:
            ps = conn.prepareStatement("UPDATE mu_items SET bind = ? WHERE item_id = ?");
            ps.setBoolean(1, packet.readBoolean());
            ps.setLong(2, itemId);
            break;
         case 4:
            ps = conn.prepareStatement("UPDATE mu_items SET container_type = ?, slot = ?,count = ? WHERE item_id = ?");
            ps.setInt(1, packet.readByte());
            ps.setInt(2, packet.readShort());
            ps.setInt(3, packet.readInt());
            ps.setLong(4, itemId);
            break;
         case 5:
            ps = conn.prepareStatement("UPDATE mu_items SET star_level = ?, once_max_star_level = ?, star_up_times = ?, bind = ?,runes = ? WHERE item_id = ?");
            ps.setInt(1, packet.readByte());
            ps.setInt(2, packet.readByte());
            ps.setInt(3, packet.readInt());
            ps.setBoolean(4, packet.readBoolean());
            ps.setString(5, packet.readUTF());
            ps.setLong(6, itemId);
            break;
         case 6:
            ps = conn.prepareStatement("UPDATE mu_items SET basis_stats = ? WHERE item_id = ?");
            ps.setString(1, packet.readUTF());
            ps.setLong(2, itemId);
            break;
         case 7:
            ps = conn.prepareStatement("UPDATE mu_items SET other_stats = ? WHERE item_id = ?");
            ps.setString(1, packet.readUTF());
            ps.setLong(2, itemId);
            break;
         case 8:
            ps = conn.prepareStatement("UPDATE mu_items SET basis_stats = ? ,other_stats = ? WHERE item_id = ?");
            ps.setString(1, packet.readUTF());
            ps.setString(2, packet.readUTF());
            ps.setLong(3, itemId);
            break;
         case 9:
            ps = conn.prepareStatement("UPDATE mu_items SET stones = ? WHERE item_id = ?");
            ps.setString(1, packet.readUTF());
            ps.setLong(2, itemId);
            break;
         case 10:
            ps = conn.prepareStatement("UPDATE mu_items SET runes = ?,bind = ? WHERE item_id = ?");
            ps.setString(1, packet.readUTF());
            ps.setBoolean(2, true);
            ps.setLong(3, itemId);
            break;
         case 11:
            ps = conn.prepareStatement("UPDATE mu_items SET durability = ? WHERE item_id = ?");
            ps.setInt(1, packet.readShort());
            ps.setLong(2, itemId);
            break;
         case 12:
            ps = conn.prepareStatement("UPDATE mu_items SET container_type = ?,slot = ?,money = ? ,money_type = ? WHERE item_id = ?");
            ps.setInt(1, packet.readByte());
            ps.setInt(2, packet.readShort());
            ps.setInt(3, packet.readInt());
            ps.setInt(4, packet.readByte());
            ps.setLong(5, itemId);
            break;
         case 13:
            ps = conn.prepareStatement("UPDATE mu_items SET slot = ?,count = ? WHERE item_id = ?");
            ps.setInt(1, packet.readShort());
            ps.setInt(2, packet.readInt());
            ps.setLong(3, itemId);
            break;
         case 14:
            ps = conn.prepareStatement("UPDATE mu_items SET zhuijia_level = ? WHERE item_id = ?");
            ps.setInt(1, packet.readByte());
            ps.setLong(2, itemId);
            break;
         case 15:
            ps = conn.prepareStatement("UPDATE mu_items SET model_id = ?,SOCKET = ? WHERE item_id = ?");
            ps.setInt(1, packet.readInt());
            ps.setInt(2, packet.readByte());
            ps.setLong(3, itemId);
         }

         if (ps != null) {
            ps.executeUpdate();
            ps.close();
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deleteItem(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         long itemId = packet.readLong();
         PreparedStatement ps = conn.prepareStatement("DELETE FROM mu_items WHERE item_id = ?");
         ps.setLong(1, itemId);
         ps.execute();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void updateStorage(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         long roleId = packet.readLong();
         int containerType = packet.readByte();
         int page = packet.readShort();
         int cooledCount = packet.readShort();
         int cooledTime = packet.readInt();
         PreparedStatement ps = conn.prepareStatement("REPLACE INTO mu_storage(role_id ,container_type,grid_count,cooled_count,cooled_time) VALUES(?,?,?,?,?)");
         ps.setLong(1, roleId);
         ps.setInt(2, containerType);
         ps.setInt(3, page);
         ps.setInt(4, cooledCount);
         ps.setInt(5, cooledTime);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static List searchStorage(long roleID) {
      Connection conn = Pool.getConnection();
      ArrayList storages = new ArrayList();

      try {
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM mu_storage WHERE role_id = ?");
         ps.setLong(1, roleID);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int containerType = rs.getInt("container_type");
            int page = rs.getInt("grid_count");
            int cooledCount = rs.getInt("cooled_count");
            int cooledTime = rs.getInt("cooled_time");
            storages.add(new int[]{containerType, page, cooledCount, cooledTime});
         }

         rs.close();
         ps.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return storages;
   }

   public static void updateItemLimit(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         long roleId = packet.readLong();
         int limitModelID = packet.readInt();
         int limitCount = packet.readInt();
         long day = Time.getDayLong();
         PreparedStatement ps = conn.prepareStatement("REPLACE INTO role_item_limits(role_id,limit_model_id,limit_day,limit_count) VALUES(?,?,?,?)");
         ps.setLong(1, roleId);
         ps.setInt(2, limitModelID);
         ps.setLong(3, day);
         ps.setInt(4, limitCount);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static HashMap searchItemLimit(long roleID) {
      Connection conn = Pool.getConnection();
      PreparedStatement ps = null;
      HashMap map = null;

      try {
         long day = Time.getDayLong();
         ps = conn.prepareStatement("SELECT limit_model_id,limit_count FROM role_item_limits WHERE role_id = ? AND limit_day = ? ");
         ps.setLong(1, roleID);
         ps.setLong(2, day);

         ResultSet rs;
         int modelID;
         int count;
         for(rs = ps.executeQuery(); rs.next(); map.put(modelID, count)) {
            modelID = rs.getInt(1);
            count = rs.getInt(2);
            if (map == null) {
               map = new HashMap();
            }
         }

         rs.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeConnection(conn);
      }

      return map;
   }
}
