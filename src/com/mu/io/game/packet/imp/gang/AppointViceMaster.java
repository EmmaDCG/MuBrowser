package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class AppointViceMaster extends ReadAndWritePacket {
   public AppointViceMaster(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      if (gang != null) {
         long rid = (long)this.readDouble();
         boolean b = this.readBoolean();
         if (b) {
            gang.doOperation(player, 11, rid);
         } else {
            gang.doOperation(player, 12, rid);
         }

      }
   }
}
