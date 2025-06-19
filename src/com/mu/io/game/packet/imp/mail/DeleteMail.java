package com.mu.io.game.packet.imp.mail;

import com.mu.game.model.mail.Mail;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;

public class DeleteMail extends ReadAndWritePacket {
   public DeleteMail(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long id = (long)this.readDouble();
      Mail mail = player.getMailManager().getMail(id);
      if (mail != null) {
         if (mail.hasItem()) {
            SystemMessage.writeMessage(player, 13002);
         } else {
            ArrayList list = new ArrayList();
            list.add(id);
            ServerDeleteMail.deleteMail(player, list);
            list.clear();
            list = null;
         }
      }
   }
}
