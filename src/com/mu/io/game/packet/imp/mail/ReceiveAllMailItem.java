package com.mu.io.game.packet.imp.mail;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class ReceiveAllMailItem extends ReadAndWritePacket {
   public ReceiveAllMailItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int result = player.getMailManager().receiveAllItem();
      if (result == 1) {
         this.writeBoolean(true);
      } else {
         SystemMessage.writeMessage(player, result);
         this.writeBoolean(false);
      }

      player.writePacket(this);
   }
}
