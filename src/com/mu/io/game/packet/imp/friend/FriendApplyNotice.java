package com.mu.io.game.packet.imp.friend;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class FriendApplyNotice extends WriteOnlyPacket {
   public FriendApplyNotice() {
      super(11211);
   }

   public static void notice(Player player, long rid) {
      FriendApplyNotice fn = new FriendApplyNotice();

      try {
         fn.writeDouble((double)rid);
         player.writePacket(fn);
         fn.destroy();
         fn = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
