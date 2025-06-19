package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.CenterManager;
import com.mu.game.IDFactory;
import com.mu.game.model.item.Item;
import com.mu.game.model.mail.Mail;
import com.mu.game.model.mail.MailItem;
import com.mu.game.model.mail.MailItemData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

public class MailDBManager {
   private static final String sqlGetMail = "select * from mu_mail where role_id = ? and is_delete = 0";
   private static final String sqlGetMailItem = "select a.* from mu_mail_items a,mu_mail b where a.mail_id = b.mail_id and b.role_id = ? and b.is_delete = 0 and a.is_delete = 0 order by item_index";
   private static final String insertMail = "insert into mu_mail (mail_id,role_id,mail_title,mail_content,mail_time,isread,expired_time) values (?,?,?,?,?,?,?)";
   private static final String insertMailItem = "insert into mu_mail_items values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   private static final String deleteMail = "update mu_mail set is_delete = 1 where mail_id = ?";
   private static final String deleteMailItem = "update mu_mail_items set is_delete = 1 where mail_id = ?";
   private static final String sqlReadMail = "update mu_mail set isread = 1 where mail_id = ?";

   public static ArrayList getMailList(long rid) {
      Connection conn = null;
      ArrayList mailList = new ArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select * from mu_mail where role_id = ? and is_delete = 0");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long id = rs.getLong("mail_id");
            String title = rs.getString("mail_title");
            String content = rs.getString("mail_content");
            long time = rs.getLong("mail_time");
            boolean isRead = rs.getInt("isread") == 1;
            long expiredTime = rs.getLong("expired_time");
            if (content == null) {
               content = "";
            }

            Mail mail = new Mail(id);
            mail.setTitle(title);
            mail.setRoleId(rid);
            mail.setTime(time);
            mail.setContent(content);
            mail.setRead(isRead);
            mail.setExpiredTime(expiredTime);
            mailList.add(mail);
         }

         rs.close();
         ps.close();
         rs = null;
         ps = null;
      } catch (Exception var19) {
         var19.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return mailList;
   }

   public static ArrayList getMailItemList(long rid) {
      Connection conn = null;
      ArrayList list = new ArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select a.* from mu_mail_items a,mu_mail b where a.mail_id = b.mail_id and b.role_id = ? and b.is_delete = 0 and a.is_delete = 0 order by item_index");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long mailId = rs.getLong("mail_id");
            int index = rs.getInt("item_index");
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
            MailItemData data = new MailItemData(mailId, index);
            data.setModelID(modelID);
            data.setCount(count);
            data.setSlot(slot);
            data.setContainerType(containerType);
            data.setStarLevel(starLevel);
            data.setSocket(socket);
            data.setBind(bind);
            data.setMoney(money);
            data.setMoneyType(moneyType);
            data.setStarUpTimes(starUpTimes);
            data.setOnceMaxStarLevel(onceMaxStarLevel);
            data.setExpireTime(expireTime);
            data.setDurability(durability);
            data.setBasisStats(basisStats);
            data.setOtherStats(otherStats);
            data.setStones(stones);
            data.setRunes(runes);
            data.setQuality(quality);
            data.setZhuijiaLevel(zhuijiaLevel);
            list.add(data);
         }

         rs.close();
         ps.close();
      } catch (Exception var33) {
         var33.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void readMail(long mailId) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_mail set isread = 1 where mail_id = ?");
         ps.setLong(1, mailId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deleteMail(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement psDelMail = conn.prepareStatement("update mu_mail set is_delete = 1 where mail_id = ?");
         PreparedStatement psDelItem = conn.prepareStatement("update mu_mail_items set is_delete = 1 where mail_id = ?");
         int size = packet.readShort();

         for(int i = 0; i < size; ++i) {
            long id = packet.readLong();
            psDelMail.setLong(1, id);
            psDelItem.setLong(1, id);
            psDelMail.addBatch();
            psDelItem.addBatch();
         }

         psDelItem.executeBatch();
         psDelMail.executeBatch();
         psDelItem.close();
         psDelMail.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deleteMailItem(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_mail_items set is_delete = 1 where mail_id = ?");
         int size = packet.readShort();

         for(int i = 0; i < size; ++i) {
            long id = packet.readLong();
            ps.setLong(1, id);
            ps.addBatch();
         }

         ps.executeBatch();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveLocalServerMail(Mail mail, ArrayList roleList) {
      Connection conn = null;
      PreparedStatement ps = null;
      PreparedStatement psItem = null;

      try {
         conn = Pool.getConnection();
         ps = conn.prepareStatement("insert into mu_mail (mail_id,role_id,mail_title,mail_content,mail_time,isread,expired_time) values (?,?,?,?,?,?,?)");
         ArrayList itemList = mail.getItemList();
         int itemSize = itemList == null ? 0 : itemList.size();
         if (itemSize > 0) {
            psItem = conn.prepareStatement("insert into mu_mail_items values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
         }

         int executeSize = 0;
         Iterator var10 = roleList.iterator();

         while(var10.hasNext()) {
            long rid = ((Long)var10.next()).longValue();
            long mailId = IDFactory.getMailID();
            ps.setLong(1, mailId);
            ps.setLong(2, rid);
            ps.setString(3, mail.getTitle());
            ps.setString(4, mail.getContent());
            ps.setLong(5, mail.getTime());
            ps.setInt(6, mail.isRead() ? 1 : 0);
            ps.setLong(7, mail.getExpiredTime());
            ps.addBatch();
            if (itemSize > 0) {
               for(int i = 0; i < itemSize; ++i) {
                  Item item = ((MailItem)itemList.get(i)).getItem();
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
                  long itemExpireTime = item.getExpireTime();
                  int durability = item.getDurability();
                  String basisStats = item.getBasisStr();
                  String otherStats = item.getOtherStr();
                  String stones = item.getStoneStr();
                  String runes = item.getRuneStr();
                  int zhuijiaLevel = item.getZhuijiaLevel();
                  psItem.setLong(1, mailId);
                  psItem.setInt(2, i);
                  psItem.setInt(3, modelID);
                  psItem.setInt(4, quality);
                  psItem.setInt(5, count);
                  psItem.setInt(6, slot);
                  psItem.setInt(7, containerType);
                  psItem.setInt(8, starLevel);
                  psItem.setInt(9, socket);
                  psItem.setBoolean(10, bind);
                  psItem.setInt(11, money);
                  psItem.setInt(12, moneyType);
                  psItem.setInt(13, starUpTimes);
                  psItem.setInt(14, onceMaxStarLevel);
                  psItem.setLong(15, itemExpireTime);
                  psItem.setInt(16, durability);
                  psItem.setString(17, basisStats);
                  psItem.setString(18, otherStats);
                  psItem.setString(19, stones);
                  psItem.setString(20, runes);
                  psItem.setInt(21, zhuijiaLevel);
                  psItem.setInt(22, 0);
                  psItem.addBatch();
               }
            }

            ++executeSize;
            if (executeSize % 500 == 0) {
               ps.executeBatch();
               if (psItem != null) {
                  psItem.executeBatch();
               }
            }

            Player player = CenterManager.getPlayerByRoleID(rid);
            if (player != null) {
               Mail cloneMail = mail.cloneMail();
               cloneMail.setRoleId(rid);
               player.getMailManager().addMail(cloneMail);
            }
         }

         ps.executeBatch();
         if (psItem != null) {
            psItem.executeBatch();
         }
      } catch (Exception var38) {
         var38.printStackTrace();
      } finally {
         Pool.closeStatment(ps);
         Pool.closeStatment(psItem);
         Pool.closeConnection(conn);
      }

   }

   public static void insertMail(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("insert into mu_mail (mail_id,role_id,mail_title,mail_content,mail_time,isread,expired_time) values (?,?,?,?,?,?,?)");
         long mailId = packet.readLong();
         long rid = packet.readLong();
         String title = packet.readUTF();
         String content = packet.readUTF();
         long time = packet.readLong();
         int isRead = packet.readBoolean() ? 1 : 0;
         long expireTime = packet.readLong();
         ps.setLong(1, mailId);
         ps.setLong(2, rid);
         ps.setString(3, title);
         ps.setString(4, content);
         ps.setLong(5, time);
         ps.setInt(6, isRead);
         ps.setLong(7, expireTime);
         ps.executeUpdate();
         ps.close();
         int itemSize = packet.readByte();
         if (itemSize > 0) {
            PreparedStatement psItem = conn.prepareStatement("insert into mu_mail_items values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            for(int i = 0; i < itemSize; ++i) {
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
               long itemExpireTime = packet.readLong();
               int durability = packet.readShort();
               String basisStats = packet.readUTF();
               String otherStats = packet.readUTF();
               String stones = packet.readUTF();
               String runes = packet.readUTF();
               int zhuijiaLevel = packet.readByte();
               psItem.setLong(1, mailId);
               psItem.setInt(2, i);
               psItem.setInt(3, modelID);
               psItem.setInt(4, quality);
               psItem.setInt(5, count);
               psItem.setInt(6, slot);
               psItem.setInt(7, containerType);
               psItem.setInt(8, starLevel);
               psItem.setInt(9, socket);
               psItem.setBoolean(10, bind);
               psItem.setInt(11, money);
               psItem.setInt(12, moneyType);
               psItem.setInt(13, starUpTimes);
               psItem.setInt(14, onceMaxStarLevel);
               psItem.setLong(15, itemExpireTime);
               psItem.setInt(16, durability);
               psItem.setString(17, basisStats);
               psItem.setString(18, otherStats);
               psItem.setString(19, stones);
               psItem.setString(20, runes);
               psItem.setInt(21, zhuijiaLevel);
               psItem.setInt(22, 0);
               psItem.addBatch();
            }

            psItem.executeBatch();
            psItem.close();
         }
      } catch (Exception var40) {
         var40.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
