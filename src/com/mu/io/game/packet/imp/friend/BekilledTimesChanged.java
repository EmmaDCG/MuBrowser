package com.mu.io.game.packet.imp.friend;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class BekilledTimesChanged extends WriteOnlyPacket {
   public BekilledTimesChanged() {
      super(11210);
   }

   public static void changed(Player player, long rid, int times) {
      try {
         BekilledTimesChanged bc = new BekilledTimesChanged();
         bc.writeDouble((double)rid);
         bc.writeShort(times);
         player.writePacket(bc);
         bc.destroy();
         bc = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
