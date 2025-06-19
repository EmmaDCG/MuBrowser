package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class AddExpMessage extends WriteOnlyPacket {
   public AddExpMessage() {
      super(1006);
   }

   public static void writeMessage(Player player, long exp) {
      try {
         AddExpMessage am = new AddExpMessage();
         am.writeDouble((double)exp);
         player.writePacket(am);
         am.destroy();
         am = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
