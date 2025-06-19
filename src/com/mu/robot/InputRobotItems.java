package com.mu.robot;

import com.mu.db.Pool;
import com.mu.game.IDFactory;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.model.ItemType;
import com.mu.utils.Rnd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InputRobotItems {
   private static final String sqlAddItem = "INSERT INTO mu_items(item_id,role_id,model_id,COUNT,slot,container_type,star_level,SOCKET,bind,money,money_type,star_up_times,once_max_star_level,expire_time,durability,basis_stats,other_stats,stones,runes) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   private static int[] modelIDs = new int[]{1105131, 1105151, 1105161, 1105181, 1105211};

   public static void main(String[] args) {
   }

   public static int insertItem() {
      int result = 1;
      Connection conn = Pool.getConnection();

      try {
         List roleIDList = new ArrayList();
         PreparedStatement rolePs = conn.prepareStatement("SELECT role_id FROM mu_role WHERE user_name LIKE 't%'");
         ResultSet rs = rolePs.executeQuery();

         while(rs.next()) {
            roleIDList.add(rs.getLong(1));
         }

         rs.close();
         rolePs.close();
         int count = 1;
         int slot = 0;
         int containerType = 0;
         int starLevel = Rnd.get(10);
         boolean bind = true;
         int money = 1;
         int moneyType = 1;
         int starUpTimes = 0;
         int onceMaxStarLevel = starLevel;
         long expireTime = -1L;
         int durability = 100;
         String basisStats = "";
         String otherStats = "";
         String stones = "";
         String runes = "";
         PreparedStatement ps = conn.prepareStatement("INSERT INTO mu_items(item_id,role_id,model_id,COUNT,slot,container_type,star_level,SOCKET,bind,money,money_type,star_up_times,once_max_star_level,expire_time,durability,basis_stats,other_stats,stones,runes) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
         Iterator var23 = roleIDList.iterator();

         while(var23.hasNext()) {
            Long roleID = (Long)var23.next();
            int[] var27 = modelIDs;
            int var26 = modelIDs.length;

            for(int var25 = 0; var25 < var26; ++var25) {
               Integer modelID = var27[var25];
               ItemModel model = ItemModel.getModel(modelID.intValue());
               if (model == null) {
                  System.out.println("没有想要的模板ID " + modelID);
               } else {
                  ps.setLong(1, IDFactory.getItemID());
                  ps.setLong(2, roleID.longValue());
                  ps.setInt(3, modelID.intValue());
                  ps.setInt(4, count);
                  ps.setInt(5, ((Integer)ItemType.getEquipSlot(model.getItemType()).get(0)).intValue());
                  ps.setInt(6, containerType);
                  ps.setInt(7, 0);
                  ps.setInt(8, Rnd.get(3));
                  ps.setBoolean(9, bind);
                  ps.setInt(10, money);
                  ps.setInt(11, moneyType);
                  ps.setInt(12, starUpTimes);
                  ps.setInt(13, onceMaxStarLevel);
                  ps.setLong(14, expireTime);
                  ps.setInt(15, durability);
                  ps.setString(16, basisStats);
                  ps.setString(17, otherStats);
                  ps.setString(18, stones);
                  ps.setString(19, runes);
                  ps.addBatch();
               }
            }
         }

         ps.executeBatch();
         ps.close();
      } catch (Exception var32) {
         var32.printStackTrace();
         result = 2;
      } finally {
         System.out.println("事情做完了");
         Pool.closeConnection(conn);
      }

      return result;
   }
}
