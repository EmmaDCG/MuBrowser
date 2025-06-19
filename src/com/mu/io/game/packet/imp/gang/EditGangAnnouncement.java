package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class EditGangAnnouncement extends ReadAndWritePacket {
   public EditGangAnnouncement(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      if (gang != null) {
         String des = this.readUTF();
         if (des.length() > 68) {
            SystemMessage.writeMessage(player, 9073);
         } else {
            gang.doOperation(player, 2, des);
         }
      }

   }
}
