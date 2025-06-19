package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemSaveAide;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.market.MarketItem;
import com.mu.game.model.market.MarketManager;
import com.mu.game.model.market.record.MarketRecord;
import com.mu.utils.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarketDBManager {
   private static Logger logger = LoggerFactory.getLogger(MarketDBManager.class);
   private static final String sqlAddMarketItem = "INSERT INTO mu_market(item_id,role_id,model_id,quality,COUNT,slot,container_type,star_level,SOCKET,bind,money,money_type,star_up_times,once_max_star_level,expire_time,durability,basis_stats,other_stats,stones,runes,zhuijia_level,role_name,user_name,shelve_time,shelve_Calendar,server_id,tax_rate)  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   private static final String sqlSearchItems = "SELECT * FROM mu_market";
   private static final String sqlDelItem = "DELETE FROM mu_market WHERE item_id = ?";
   private static final String sqlSaveRecord = "INSERT INTO market_records VALUES(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?)";

   public static int insertMarketItem(MarketItem marketItem) {
      int result = 1;
      Connection conn = Pool.getConnection();

      try {
         PreparedStatement ps = conn.prepareStatement("INSERT INTO mu_market(item_id,role_id,model_id,quality,COUNT,slot,container_type,star_level,SOCKET,bind,money,money_type,star_up_times,once_max_star_level,expire_time,durability,basis_stats,other_stats,stones,runes,zhuijia_level,role_name,user_name,shelve_time,shelve_Calendar,server_id,tax_rate)  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
         ItemDBManager.insertDetial(marketItem.getRoleID(), marketItem.getItem(), ps);
         ps.setString(22, marketItem.getOwnerName());
         ps.setString(23, marketItem.getUserName());
         ps.setLong(24, marketItem.getShelveTime());
         ps.setString(25, Time.getTimeStr(marketItem.getShelveTime()));
         ps.setInt(26, marketItem.getServerID());
         ps.setInt(27, marketItem.getTaxRate());
         ps.executeUpdate();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
         result = 2;
      } finally {
         Pool.closeConnection(conn);
      }

      return result;
   }

   public static void searchAllMarketItem() {
      Connection conn = Pool.getConnection();

      try {
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM mu_market");
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long roleID = rs.getLong("role_id");
            ItemSaveAide isa = ItemDBManager.initISA(rs);
            String ownerName = rs.getString("role_name");
            long shelveTime = rs.getLong("shelve_time");
            String userName = rs.getString("user_name");
            int serverID = rs.getInt("server_id");
            int taxRate = rs.getInt("tax_rate");
            Item item = ItemTools.loadItem(isa);
            if (item == null) {
               logger.debug("市场物品不存在" + isa.getItemId());
            } else {
               MarketItem marketItem = new MarketItem(item);
               marketItem.setShelveTime(shelveTime);
               marketItem.setOwnerName(ownerName);
               marketItem.setUserName(userName);
               marketItem.setRoleID(roleID);
               marketItem.setServerID(serverID);
               marketItem.setTaxRate(taxRate);
               MarketManager.addMarketItem(marketItem);
            }
         }

         rs.close();
         ps.close();
      } catch (Exception var17) {
         var17.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void delMarketItem(long itemID) {
      Connection conn = Pool.getConnection();

      try {
         PreparedStatement ps = conn.prepareStatement("DELETE FROM mu_market WHERE item_id = ?");
         ps.setLong(1, itemID);
         ps.execute();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveRecord(MarketRecord record) {
      Connection conn = Pool.getConnection();

      try {
         Item item = record.getItem();
         PreparedStatement ps = conn.prepareStatement("INSERT INTO market_records VALUES(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?)");
         ps.setLong(1, record.getGoodID());
         ps.setInt(2, item.getModelID());
         ps.setString(3, item.getName());
         ps.setLong(4, record.getSalerID());
         ps.setString(5, record.getSalerName());
         ps.setLong(6, record.getBuyerID());
         ps.setString(7, record.getBuyerName());
         ps.setInt(8, item.getQuality());
         ps.setInt(9, item.getCount());
         ps.setInt(10, item.getStarLevel());
         ps.setInt(11, item.getZhuijiaLevel());
         ps.setInt(12, item.getSocket());
         ps.setString(13, item.getBasisStr());
         ps.setString(14, item.getOtherStr());
         ps.setString(15, item.getStoneStr());
         ps.setString(16, item.getRuneStr());
         ps.setInt(17, record.getGainMoney());
         ps.setInt(18, record.getTax());
         ps.setString(19, Time.getTimeStr(System.currentTimeMillis()));
         ps.execute();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
