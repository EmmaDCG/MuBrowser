package com.mu.io.game.packet.imp.item;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class AutoUseItem extends WriteOnlyPacket {
   public AutoUseItem() {
      super(20031);
   }

   public static void sendToClient(Player player, int type) {
      try {
         AutoUseItem au = new AutoUseItem();
         au.writeByte(type);
         player.writePacket(au);
         au.destroy();
         au = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
