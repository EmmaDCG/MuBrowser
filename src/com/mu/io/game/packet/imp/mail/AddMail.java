package com.mu.io.game.packet.imp.mail;

import com.mu.game.model.mail.Mail;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.sys.ListPacket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class AddMail extends WriteOnlyPacket {
   public AddMail(ConcurrentHashMap mailMap) {
      super(10902);

      try {
         this.writeShort(mailMap.size());
         Iterator it = mailMap.values().iterator();

         while(it.hasNext()) {
            Mail mail = (Mail)it.next();
            this.writeDouble((double)mail.getID());
            this.writeUTF(mail.getTitle());
            this.writeDouble((double)mail.getTime());
            this.writeBoolean(mail.isRead());
            this.writeBoolean(mail.hasItem());
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public AddMail(Mail mail) {
      super(10902);

      try {
         this.writeShort(1);
         this.writeDouble((double)mail.getID());
         this.writeUTF(mail.getTitle());
         this.writeDouble((double)mail.getTime());
         this.writeBoolean(mail.isRead());
         this.writeBoolean(mail.hasItem());
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void addNewMail(Player player, Mail mail) {
      if (player != null) {
         AddMail am = new AddMail(mail);
         player.writePacket(am);
         am.destroy();
         am = null;
      }
   }

   public static void pushAllMail(Player player) {
      try {
         ConcurrentHashMap map = player.getMailManager().getAllMails();
         int size = map.size();
         if (size == 0) {
            return;
         }

         AddMail am = new AddMail(map);
         if (size > 20) {
            ListPacket lp = ListPacket.forClient();
            lp.addPacket(am);
            player.writePacket(lp);
            lp.destroy();
            lp = null;
         } else {
            player.writePacket(am);
         }

         am.destroy();
         am = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
