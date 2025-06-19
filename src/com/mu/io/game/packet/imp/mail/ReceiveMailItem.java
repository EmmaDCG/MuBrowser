package com.mu.io.game.packet.imp.mail;

import com.mu.game.model.mail.Mail;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class ReceiveMailItem extends ReadAndWritePacket {
   public ReceiveMailItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long id = (long)this.readDouble();
      Mail mail = player.getMailManager().getMail(id);
      if (mail != null) {
         int result = player.getMailManager().receiveItem(mail);
         if (result == 1) {
            this.writeDouble((double)id);
            player.writePacket(this);
         } else {
            SystemMessage.writeMessage(player, result);
         }

      }
   }
}
