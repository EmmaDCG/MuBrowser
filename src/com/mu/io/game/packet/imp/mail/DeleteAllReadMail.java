package com.mu.io.game.packet.imp.mail;

import com.mu.game.model.mail.Mail;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class DeleteAllReadMail extends ReadAndWritePacket {
   public DeleteAllReadMail(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      ArrayList list = new ArrayList();
      Iterator it = player.getMailManager().getAllMails().values().iterator();

      while(it.hasNext()) {
         Mail mail = (Mail)it.next();
         if (mail.isRead() && !mail.hasItem()) {
            list.add(mail.getID());
         }
      }

      if (list.size() > 0) {
         ServerDeleteMail.deleteMail(player, list);
         list.clear();
         list = null;
      }

   }
}
