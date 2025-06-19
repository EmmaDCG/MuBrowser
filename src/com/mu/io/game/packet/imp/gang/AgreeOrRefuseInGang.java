package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class AgreeOrRefuseInGang extends ReadAndWritePacket {
   public AgreeOrRefuseInGang(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long rid = (long)this.readDouble();
      boolean isAgree = this.readBoolean();
      Gang gang = player.getGang();
      if (gang != null) {
         if (isAgree) {
            gang.doOperation(player, 4, rid);
         } else {
            gang.doOperation(player, 5, rid);
         }

      }
   }
}
