package com.mu.game.model.mail;

import com.mu.game.IDFactory;
import java.util.ArrayList;
import java.util.Iterator;

public class Mail {
   private long ID;
   private long roleId;
   private long senderId = -1L;
   private String senderName = "";
   private String title = null;
   private long time = 0L;
   private long expiredTime = 0L;
   private boolean isRead = false;
   private String content = null;
   private ArrayList itemList = new ArrayList(8);

   public Mail(long id) {
      this.ID = id;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public boolean hasItem() {
      return this.itemList.size() > 0;
   }

   public long getExpiredTime() {
      return this.expiredTime;
   }

   public void setExpiredTime(long expiredTime) {
      this.expiredTime = expiredTime;
   }

   public String getSenderName() {
      return this.senderName;
   }

   public void setSenderName(String senderName) {
      this.senderName = senderName;
   }

   public long getTime() {
      return this.time;
   }

   public void setTime(long time) {
      this.time = time;
   }

   public boolean isRead() {
      return this.isRead;
   }

   public void readMail() {
   }

   public void setRead(boolean read) {
      this.isRead = read;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public long getID() {
      return this.ID;
   }

   public void addItem(MailItem item) {
      this.itemList.add(item);
   }

   public long getRoleId() {
      return this.roleId;
   }

   public void setRoleId(long rid) {
      this.roleId = rid;
   }

   public long getSenderId() {
      return this.senderId;
   }

   public void setSenderId(long senderID) {
      this.senderId = senderID;
   }

   public ArrayList getItemList() {
      return this.itemList;
   }

   public void setItemList(ArrayList list) {
      this.itemList = list;
   }

   public void clearItems() {
      try {
         Iterator var2 = this.itemList.iterator();

         while(var2.hasNext()) {
            MailItem mi = (MailItem)var2.next();
            mi.destroy();
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      } finally {
         this.itemList.clear();
      }

   }

   public void destroy() {
      if (this.itemList != null) {
         this.clearItems();
         this.itemList = null;
      }

      this.title = null;
      this.content = null;
   }

   public Mail cloneMail() {
      Mail mail = new Mail(IDFactory.getMailID());
      mail.setContent(this.getContent());
      mail.setExpiredTime(this.getExpiredTime());
      mail.setRead(this.isRead);
      mail.setTime(this.getTime());
      mail.setTitle(this.getTitle());
      if (this.itemList != null) {
         for(int i = 0; i < this.itemList.size(); ++i) {
            MailItem mi = (MailItem)this.itemList.get(i);
            MailItem newItem = new MailItem(i);
            newItem.setItem(mi.getItem().cloneItem(2));
         }
      }

      return mail;
   }
}
