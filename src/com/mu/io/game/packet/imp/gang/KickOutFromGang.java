package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class KickOutFromGang extends ReadAndWritePacket {
   public KickOutFromGang(boolean b) {
      super(10617, (byte[])null);

      try {
         this.writeBoolean(b);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public KickOutFromGang(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      if (gang != null) {
         gang.doOperation(player, 8, (long)this.readDouble());
      }

   }
}
