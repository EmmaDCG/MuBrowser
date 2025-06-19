package com.mu.game.model.mail;

import com.mu.executor.imp.mail.DeleteMailItemExecutor;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class MailManager {
   public static final long SaveTime = 2592000000L;
   private ConcurrentHashMap mailMap = new ConcurrentHashMap(8, 0.75F, 2);
   private Player owner;
   private int unReadSize = 0;

   public MailManager(Player owner) {
      this.owner = owner;
   }

   public ConcurrentHashMap getAllMails() {
      return this.mailMap;
   }

   public void addMail(Mail mail) {
      this.mailMap.put(mail.getID(), mail);
   }

   public Mail getMail(long id) {
      return (Mail)this.mailMap.get(id);
   }

   public boolean hasMail(long id) {
      return this.mailMap.containsKey(id);
   }

   public Mail removeMail(long id) {
      return (Mail)this.mailMap.remove(id);
   }

   public void deleteMailsNotFormDB(ArrayList mailIDs) {
   }

   public Player getOwner() {
      return this.owner;
   }

   public void initUnreadSize() {
      this.unReadSize = 0;
      Iterator it = this.mailMap.values().iterator();

      while(it.hasNext()) {
         if (!((Mail)it.next()).isRead()) {
            ++this.unReadSize;
         }
      }

   }

   public boolean hasNewMail() {
      Iterator it = this.mailMap.values().iterator();

      while(it.hasNext()) {
         Mail mail = (Mail)it.next();
         if (!mail.isRead()) {
            return true;
         }
      }

      return false;
   }

   public int receiveItem(Mail mail) {
      ArrayList itemList = new ArrayList();

      try {
         if (!mail.hasItem()) {
            return 13003;
         }

         ArrayList mList = mail.getItemList();
         int needCount = 0;

         MailItem mi;
         Iterator var6;
         Item item;
         for(var6 = mList.iterator(); var6.hasNext(); itemList.add(item)) {
            mi = (MailItem)var6.next();
            item = mi.getItem();
            if (!item.getModel().isnotStorage()) {
               ++needCount;
            }
         }

         if (this.owner.getBackpack().getVacantSize() >= needCount) {
            var6 = itemList.iterator();

            while(var6.hasNext()) {
               Item item3 = (Item)var6.next();
               this.owner.getItemManager().addItem(item3, 27);
            }

            mail.clearItems();
            ArrayList list = new ArrayList();
            list.add(mail.getID());
            DeleteMailItemExecutor.deleteItem(this.owner, list);
            list.clear();
            mi = null;
            return 1;
         }
      } finally {
         itemList.clear();
         itemList = null;
      }

      return 2004;
   }

   public int receiveAllItem() {
      ArrayList itemList = new ArrayList();
      ArrayList idList = new ArrayList();
      ArrayList mList = new ArrayList();

      try {
         int count = 0;
         Iterator it = this.mailMap.values().iterator();

         while(it.hasNext()) {
            Mail mail = (Mail)it.next();
            if (mail.hasItem()) {
               mList.add(mail);
               idList.add(mail.getID());
            }
         }

         if (mList.size() > 0) {
            Iterator var14 = mList.iterator();

            while(true) {
               Mail mail;
               if (!var14.hasNext()) {
                  if (this.owner.getBackpack().getVacantSize() < count) {
                     return 2004;
                  }

                  var14 = itemList.iterator();

                  while(var14.hasNext()) {
                     Item item = (Item)var14.next();
                     this.owner.getItemManager().addItem(item, 27);
                  }

                  var14 = mList.iterator();

                  while(var14.hasNext()) {
                     mail = (Mail)var14.next();
                     mail.clearItems();
                     if (!mail.isRead()) {
                        mail.setRead(true);
                     }
                  }

                  DeleteMailItemExecutor.deleteItem(this.owner, idList);
                  break;
               }

               mail = (Mail)var14.next();
               Iterator var8 = mail.getItemList().iterator();

               while(var8.hasNext()) {
                  MailItem mi = (MailItem)var8.next();
                  itemList.add(mi.getItem());
                  if (!mi.getItem().getModel().isnotStorage()) {
                     ++count;
                  }
               }
            }
         }
      } finally {
         itemList.clear();
         itemList = null;
         idList.clear();
         idList = null;
         mList.clear();
         mList = null;
      }

      return 1;
   }

   public int getUnreadSize() {
      return this.unReadSize;
   }

   public int addUnreadMail(int size) {
      this.unReadSize += size;
      return this.unReadSize;
   }

   public int reduceUnreadMail(int size) {
      this.unReadSize -= size;
      if (this.unReadSize < 0) {
         this.unReadSize = 0;
      }

      return this.unReadSize;
   }

   public void destroy() {
      this.mailMap.clear();
      this.mailMap = null;
      this.owner = null;
   }
}
