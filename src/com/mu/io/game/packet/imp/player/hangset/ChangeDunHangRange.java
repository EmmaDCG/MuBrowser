package com.mu.io.game.packet.imp.player.hangset;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ChangeDunHangRange extends WriteOnlyPacket {
   public ChangeDunHangRange(int range) {
      super(10017);

      try {
         this.writeShort(range);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void change(Player player, int range) {
      ChangeDunHangRange cr = new ChangeDunHangRange(range);
      player.writePacket(cr);
      cr.destroy();
      cr = null;
   }
}
