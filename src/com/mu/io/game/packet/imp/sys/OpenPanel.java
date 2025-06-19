package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class OpenPanel extends WriteOnlyPacket {
   public OpenPanel() {
      super(1007);
   }

   public static void open(Player player, int bigId, int smallId) {
      OpenPanel op = new OpenPanel();

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
