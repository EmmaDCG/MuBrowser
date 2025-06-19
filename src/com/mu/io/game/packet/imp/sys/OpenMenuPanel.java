package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class OpenMenuPanel extends WriteOnlyPacket {
   public OpenMenuPanel() {
      super(1013);
   }

   public static void open(Player player, int bigId, int smallId) {
      OpenMenuPanel op = new OpenMenuPanel();

      try {
         op.writeInt(bigId);
         op.writeByte(smallId);
         player.writePacket(op);
         op.destroy();
         op = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
