package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemColor;
import com.mu.game.model.item.model.ItemConstant;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.model.ItemSource;
import com.mu.utils.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class GMDBManager {
   private static final String SQL_QUERY_ROLE = "SELECT role_id, user_name, role_name FROM mu_role WHERE user_name LIKE ? || role_name LIKE ? ORDER BY user_name";
   private static final String SQL_SET_ZJ_TASK = "update role_task set taskId=?,state=1, rateStr='' where roleId in (select role_id from mu_role where role_name=?) and type = 1 ";
   private static final String SQL_INSERT_BAN = "replace into mu_ban VALUES (?, ?, ?) ";
   private static final String SQL_DELETE_BAN = "delete from mu_ban where type=? and mark=? ";
   private static final String SQL_Query_items = "SELECT a.role_name,a.user_name,b.* FROM mu_role a LEFT JOIN  mu_items b ON a.role_id = b.role_id WHERE a.role_id IN (SELECT role_id FROM mu_role WHERE role_name LIKE ?) ORDER BY a.role_id,b.container_type limit 200";
   private static final String SQL_Query_Item_Log = "SELECT nick_name,TYPE,type_name,model_id,item_name,COUNT,log_time,source FROM mu_item_log WHERE nick_name = ? AND STR_TO_DATE(log_time,'%Y-%m-%d %H:%i:%s') BETWEEN ? AND ? ORDER BY log_time DESC LIMIT ?";

   public static void queryRoleInfo(String keyword, List lineList) throws Exception {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT role_id, user_name, role_name FROM mu_role WHERE user_name LIKE ? || role_name LIKE ? ORDER BY user_name");
         keyword = '%' + keyword + '%';
         ps.setString(1, keyword);
         ps.setString(2, keyword);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            lineList.add(new String[]{String.valueOf(rs.getLong("role_id")), rs.getString("user_name"), rs.getString("role_name")});
         }

         rs.close();
         ps.close();
      } finally {
         Pool.closeConnection(conn);
      }
   }

   public static int settingRoleZJTask(String roleName, int taskId) throws Exception {
      int count = 0;
      Connection conn = null;
      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update role_task set taskId=?,state=1, rateStr='' where roleId in (select role_id from mu_role where role_name=?) and type = 1 ");
         ps.setInt(1, taskId);
         ps.setString(2, roleName);
         count = ps.executeUpdate();
         ps.close();
      } finally {
         Pool.closeConnection(conn);
      }

      return count;
   }

   public static void insertBan(int type, String mark, String timestamp) throws Exception {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into mu_ban VALUES (?, ?, ?) ");
         ps.setInt(1, type);
         ps.setString(2, mark);
         ps.setString(3, timestamp);
         ps.executeUpdate();
         ps.close();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deleteBan(int type, String mark) throws Exception {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("delete from mu_ban where type=? and mark=? ");
         ps.setInt(1, type);
         ps.setString(2, mark);
         ps.executeUpdate();
         ps.close();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void executeQuerySQL(boolean logDB, String sql, List resultList, int limitIndex, int limitLength) throws Exception {
      Connection conn = null;

      try {
         conn = logDB ? Pool.getLogConnection() : Pool.getConnection();
         Statement stat = conn.createStatement();
         sql = sql + " limit " + limitIndex + ", " + limitLength;
         ResultSet rs = stat.executeQuery(sql);
         int columnCount = rs.getMetaData().getColumnCount();

         while(rs.next()) {
            String[] row = new String[columnCount];

            for(int i = 1; i <= row.length; ++i) {
               row[i - 1] = rs.getString(i);
            }

            resultList.add(row);
         }

         rs.close();
         stat.close();
      } finally {
         Pool.closeConnection(conn);
      }
   }

   public static int executeQueryCountSQL(boolean logDB, String sql) throws Exception {
      Connection conn = null;
      int count = 0;

      try {
         conn = logDB ? Pool.getLogConnection() : Pool.getConnection();
         Statement stat = conn.createStatement();
         ResultSet rs = stat.executeQuery("select count(*) from (" + sql + ") b");
         if (rs.next()) {
            count = rs.getInt(1);
         }

         rs.close();
         stat.close();
      } finally {
         Pool.closeConnection(conn);
      }

      return count;
   }

   public static void queryRoleItems(String keyword, List lineList) throws Exception {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT a.role_name,a.user_name,b.* FROM mu_role a LEFT JOIN  mu_items b ON a.role_id = b.role_id WHERE a.role_id IN (SELECT role_id FROM mu_role WHERE role_name LIKE ?) ORDER BY a.role_id,b.container_type limit 200");
         keyword = '%' + keyword + '%';
         ps.setString(1, keyword);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            String roleName = rs.getString("role_name");
            long itemID = rs.getLong("item_id");
            int modelId = rs.getInt("model_id");
            int slot = rs.getInt("slot");
            int containerType = rs.getInt("container_type");
            int bind = rs.getInt("bind");
            long expiredTime = rs.getLong("expire_time");
            int count = rs.getInt("count");
            int quality = rs.getInt("quality");
            int starLevel = rs.getInt("star_level");
            int zhuijiaLevel = rs.getInt("zhuijia_level");
            String stats = rs.getString("other_stats");
            String stones = rs.getString("stones");
            String runes = rs.getString("runes");
            String eTime = "永久";
            if (expiredTime != -1L) {
               eTime = Time.getTimeStr(expiredTime);
            }

            ItemModel model = ItemModel.getModel(modelId);
            if (model != null) {
               String[] excellents = ItemTools.getGmShowExllents(itemID, stats);
               String name = model.getName();
               if (model.isEquipment() && starLevel > 0) {
                  (new StringBuilder(String.valueOf(name))).append("+").append(starLevel).toString();
               }

               String[] s = new String[]{roleName, model.getName() + starLevel, ItemConstant.getContainerName(containerType), String.valueOf(slot), bind == 1 ? "是" : "否", eTime, String.valueOf(count), ItemColor.find(quality).getName(), starLevel + "星", String.valueOf(zhuijiaLevel), excellents[0], excellents[1], ItemTools.getGmShowStones(itemID, stones), ItemTools.getGMShowRuneString(itemID, runes)};
               lineList.add(s);
            }
         }

         rs.close();
         ps.close();
      } finally {
         Pool.closeConnection(conn);
      }
   }

   public static void queryItemLog(String keyword, String startTime, String endTime, String limit, List lineList) throws Exception {
      Connection conn = null;

      try {
         conn = Pool.getLogConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT nick_name,TYPE,type_name,model_id,item_name,COUNT,log_time,source FROM mu_item_log WHERE nick_name = ? AND STR_TO_DATE(log_time,'%Y-%m-%d %H:%i:%s') BETWEEN ? AND ? ORDER BY log_time DESC LIMIT ?");
         if (keyword.trim().length() >= 1) {
            ps.setString(1, keyword);
            ps.setString(2, startTime);
            ps.setString(3, endTime);
            ps.setInt(4, Integer.parseInt(limit));
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
               String roleName = rs.getString("nick_name");
               String typeName = rs.getString("type_name");
               String itemName = rs.getString("item_name");
               int count = rs.getInt("count");
               String logTime = rs.getString("log_time");
               int source = rs.getInt("source");
               String[] s = new String[]{roleName, itemName, String.valueOf(count), logTime, typeName, ItemSource.getSourceName(source)};
               lineList.add(s);
            }

            rs.close();
            ps.close();
         }
      } finally {
         Pool.closeConnection(conn);
      }
   }
}
