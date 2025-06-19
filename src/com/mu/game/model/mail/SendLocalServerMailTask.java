package com.mu.game.model.mail;

import com.mu.config.Global;
import com.mu.config.MessageText;
import com.mu.db.manager.MailDBManager;
import com.mu.game.model.item.Item;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.util.ArrayList;

public class SendLocalServerMailTask implements Runnable {
   private String title = null;
   private String content = null;
   private ArrayList itemList = null;
   private ArrayList roleList = null;

   public static void sendMail(long rid, String title, String content, ArrayList iList) {
      ArrayList list = new ArrayList(1);
      list.add(rid);
      SendLocalServerMailTask task = new SendLocalServerMailTask(list, title, content, iList);
      ThreadCachedPoolManager.DB_SHORT.execute(task);
   }

   public SendLocalServerMailTask(ArrayList roleList, String title, String content, ArrayList iList) {
      this.roleList = roleList;
      this.content = content;
      this.title = title;
      if (this.content == null) {
         this.content = "";
      }

      if (this.content.length() > 300) {
         this.content = this.content.substring(0, 299);
      }

      if (this.title == null) {
         this.title = MessageText.getText(13001);
      }

      if (this.title.length() > 8) {
         this.title = this.title.substring(0, 6) + "..";
      }

      this.itemList = iList;
   }

   public void run() {
      if (!Global.isInterServiceServer()) {
         if (this.itemList != null && this.itemList.size() != 0) {
            ArrayList tmpList = new ArrayList();
            int size = this.itemList.size();

            for(int i = 0; i < size; ++i) {
               tmpList.add((Item)this.itemList.get(i));
               if (tmpList.size() == 8 || i == size - 1) {
                  this.createMail(tmpList);
                  tmpList.clear();
               }
            }

            tmpList = null;
         } else {
            this.createMail((ArrayList)null);
         }

         this.destroy();
      }
   }

   private boolean createMail(ArrayList list) {
      Mail mail = new Mail(-1L);
      mail.setTime(System.currentTimeMillis());
      mail.setExpiredTime(mail.getTime() + 2592000000L);
      mail.setRead(false);
      mail.setTitle(this.title);
      mail.setContent(this.content);
      if (list != null && list.size() > 0) {
         for(int i = 0; i < list.size(); ++i) {
            Item item = (Item)list.get(i);
            MailItem mi = new MailItem(i);
            mi.setItem(item);
            mail.addItem(mi);
         }
      }

      MailDBManager.saveLocalServerMail(mail, this.roleList);
      mail.destroy();
      return true;
   }

   private void destroy() {
      if (this.itemList != null) {
         this.itemList.clear();
         this.itemList = null;
      }

      if (this.roleList != null) {
         this.roleList.clear();
         this.roleList = null;
      }

      this.title = null;
      this.content = null;
   }
}
