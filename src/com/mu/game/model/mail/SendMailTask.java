package com.mu.game.model.mail;

import com.mu.config.MessageText;
import com.mu.db.manager.MailDBManager;
import com.mu.executor.Executor;
import com.mu.game.CenterManager;
import com.mu.game.IDFactory;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.mail.AddMail;
import java.util.ArrayList;
import java.util.Iterator;

public class SendMailTask {
   private String title = null;
   private String content = null;
   private ArrayList itemList = null;
   private Player target = null;
   private long targetId;

   public static void sendMail(Player player, long targetId, String title, String content, ArrayList iList) {
      SendMailTask task = null;

      try {
         task = new SendMailTask(player, targetId, title, content, iList);
         task.createMail(task.itemList);
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         if (task != null) {
            task.destroy();
         }

      }

   }

   public SendMailTask(Player target, long tid, String title, String content, ArrayList iList) throws Exception {
      this.target = target;
      this.targetId = this.target == null ? tid : this.target.getID();
      if (this.target == null) {
         this.target = CenterManager.getPlayerByRoleID(this.targetId);
      }

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

      if (iList != null) {
         if (iList.size() > 8) {
            throw new Exception("Item too much");
         }

         Item item;
         for(Iterator var8 = iList.iterator(); var8.hasNext(); this.itemList.add(item)) {
            item = (Item)var8.next();
            ItemModel model = item.getModel();
            if (item.getCount() > model.getMaxStackCount()) {
               throw new Exception("Item count Error");
            }

            if (this.itemList == null) {
               this.itemList = new ArrayList();
            }
         }
      }

   }

   private boolean createMail(ArrayList list) {
      Mail mail = new Mail(IDFactory.getMailID());
      mail.setTime(System.currentTimeMillis());
      mail.setExpiredTime(mail.getTime() + 2592000000L);
      mail.setRead(false);
      mail.setRoleId(this.targetId);
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

      WriteOnlyPacket packet = Executor.SaveMail.toPacket(mail);
      if (this.target != null) {
         this.target.getMailManager().addMail(mail);
         this.target.getMailManager().addUnreadMail(1);
         this.target.writePacket(packet);
         packet.destroy();
         packet = null;
         AddMail.addNewMail(this.target, mail);
      } else {
         try {
            MailDBManager.insertMail(Executor.getDefualtExecutorPacket(packet));
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

      return true;
   }

   private void destroy() {
      if (this.itemList != null) {
         this.itemList.clear();
         this.itemList = null;
      }

      this.title = null;
      this.content = null;
      this.target = null;
   }
}
