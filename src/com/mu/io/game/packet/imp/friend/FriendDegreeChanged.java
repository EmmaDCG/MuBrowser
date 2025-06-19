package com.mu.io.game.packet.imp.friend;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class FriendDegreeChanged extends WriteOnlyPacket {
   public FriendDegreeChanged() {
      super(11209);
   }

   public static void degreeChange(Player player, long rid, int degree, boolean beBlessed) {
      try {
         FriendDegreeChanged fg = new FriendDegreeChanged();
         fg.writeDouble((double)rid);
         fg.writeInt(degree);
         fg.writeBoolean(beBlessed);
         player.writePacket(fg);
         fg.destroy();
         fg = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
