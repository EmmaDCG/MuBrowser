package com.mu.io.game.packet.imp.mail;

import com.mu.executor.imp.mail.ReadMailExecutor;
import com.mu.game.model.item.Item;
import com.mu.game.model.mail.Mail;
import com.mu.game.model.mail.MailItem;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.Iterator;

public class RequestMailDetail extends ReadAndWritePacket {
   public RequestMailDetail(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long id = (long)this.readDouble();
      Mail mail = player.getMailManager().getMail(id);
      if (mail != null) {
         this.writeDouble((double)id);
         this.writeUTF(mail.getContent());
         ArrayList list = mail.getItemList();
         this.writeByte(list.size());
         Iterator var7 = list.iterator();

         while(var7.hasNext()) {
            MailItem mi = (MailItem)var7.next();
            Item item = mi.getItem();
            GetItemStats.writeItem(item, this);
         }

         player.writePacket(this);
         if (!mail.isRead()) {
            mail.setRead(true);
            ReadMailExecutor.readMail(player, id);
         }

      }
   }
}
