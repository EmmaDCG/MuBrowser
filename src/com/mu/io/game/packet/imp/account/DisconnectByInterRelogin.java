package com.mu.io.game.packet.imp.account;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.DisconnectReason;

public class DisconnectByInterRelogin extends ReadAndWritePacket {
   public DisconnectByInterRelogin(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public DisconnectByInterRelogin() {
      super(105, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (player != null) {
         DisconnectReason.pushDisconnect(player.getChannel(), MessageText.getText(9));
         player.setDestroyType(3);
         player.setShouldDestroy(true);
      }

   }
}
