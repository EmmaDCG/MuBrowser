package com.mu.game.model.mail;

import com.mu.db.Pool;
import com.mu.game.CenterManager;
import com.mu.game.IDFactory;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.mail.AddMail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SendBatchMailTask {
   private String title = null;
   private String content = null;
   private ArrayList itemList = null;
   private Connection conn = null;
   private HashMap mailMap = new HashMap();
   private ArrayList ridList = new ArrayList();
   private static final String insertMail = "insert into mu_mail (mail_id,role_id,mail_title,mail_content,mail_time,isread,expired_time) values (?,?,?,?,?,?,?)";
   private static final String insertMailItem = "insert into mu_mail_items values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

   public static void sendMail(ArrayList idList, String title, String content, ArrayList iList) {
      SendBatchMailTask task = null;

      try {
         task = new SendBatchMailTask(idList, title, content, iList);
         task.createMail();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         if (task != null) {
            task.destroy();
         }

      }

   }

   public SendBatchMailTask(ArrayList idList, String title, String content, ArrayList iList) {
      Iterator var6 = idList.iterator();

      while(var6.hasNext()) {
         Long rid = (Long)var6.next();
         this.ridList.add(rid);
      }

      this.title = title;
      this.content = content;
      if (this.title.length() > 8) {
         this.title = this.title.substring(0, 6) + "..";
      }

      if (iList != null) {
         this.itemList = new ArrayList();
         var6 = iList.iterator();

         while(var6.hasNext()) {
            Item item = (Item)var6.next();
            this.itemList.add(item);
         }
      }

   }

   public void createMail() throws Exception {
      this.conn = Pool.getConnection();
      PreparedStatement psMail = this.conn.prepareStatement("insert into mu_mail (mail_id,role_id,mail_title,mail_content,mail_time,isread,expired_time) values (?,?,?,?,?,?,?)");
      PreparedStatement psItem = this.conn.prepareStatement("insert into mu_mail_items values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      Iterator var4 = this.ridList.iterator();

      while(var4.hasNext()) {
         Long rid = (Long)var4.next();
         Mail mail = new Mail(IDFactory.getMailID());
         mail.setTime(System.currentTimeMillis());
         mail.setExpiredTime(mail.getTime() + 2592000000L);
         mail.setRead(false);
         mail.setRoleId(rid.longValue());
         mail.setTitle(this.title);
         mail.setContent(this.content);
         int i;
         if (this.itemList != null && this.itemList.size() > 0) {
            for(i = 0; i < this.itemList.size(); ++i) {
               Item item = (Item)this.itemList.get(i);
               MailItem mi = new MailItem(i);
               mi.setItem(item);
               mail.addItem(mi);
            }
         }

         psMail.setLong(1, mail.getID());
         psMail.setLong(2, rid.longValue());
         psMail.setString(3, this.title);
         psMail.setString(4, this.content);
         psMail.setLong(5, mail.getTime());
         psMail.setInt(6, 0);
         psMail.setLong(7, mail.getExpiredTime());
         psMail.addBatch();

         for(i = 0; i < mail.getItemList().size(); ++i) {
            MailItem mi = (MailItem)mail.getItemList().get(i);
            Item item = mi.getItem();
            psItem.setLong(1, mail.getID());
            psItem.setInt(2, i);
            psItem.setInt(3, item.getModelID());
            psItem.setInt(4, item.getQuality());
            psItem.setInt(5, item.getCount());
            psItem.setInt(6, item.getSlot());
            psItem.setInt(7, item.getContainerType());
            psItem.setInt(8, item.getStarLevel());
            psItem.setInt(9, item.getScore());
            psItem.setBoolean(10, item.isBind());
            psItem.setInt(11, item.getMoney());
            psItem.setInt(12, item.getMoneyType());
            psItem.setInt(13, item.getStarUpTimes());
            psItem.setInt(14, item.getOnceMaxStarLevel());
            psItem.setLong(15, item.getExpireTime());
            psItem.setInt(16, item.getDurability());
            psItem.setString(17, item.getBasisStr());
            psItem.setString(18, item.getOtherStr());
            psItem.setString(19, item.getStoneStr());
            psItem.setString(20, item.getRuneStr());
            psItem.setInt(21, item.getZhuijiaLevel());
            psItem.setInt(22, 0);
            psItem.addBatch();
         }

         this.mailMap.put(rid, mail);
      }

      psMail.executeBatch();
      psItem.executeBatch();
      psMail.close();
      psItem.close();
      Iterator it = this.mailMap.values().iterator();

      while(it.hasNext()) {
         Mail mail = (Mail)it.next();
         long rid = mail.getRoleId();
         Player player = CenterManager.getPlayerByRoleID(rid);
         if (player != null) {
            player.getMailManager().addMail(mail);
            player.getMailManager().addUnreadMail(1);
            AddMail.addNewMail(player, mail);
         }
      }

   }

   private void destroy() {
      Pool.closeConnection(this.conn);
      if (this.itemList != null) {
         this.itemList.clear();
         this.itemList = null;
      }

      if (this.mailMap != null) {
         this.mailMap.clear();
         this.mailMap = null;
      }

      if (this.ridList != null) {
         this.ridList.clear();
         this.ridList = null;
      }

      this.title = null;
      this.content = null;
   }
}
