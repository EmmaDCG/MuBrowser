package com.mu.io.game.packet.imp.friend;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class FriendOnOrOffline extends WriteOnlyPacket {
   public FriendOnOrOffline() {
      super(11208);
   }

   public static void friendOnline(Player player, long rid, int type, int[] icons) {
      try {
         FriendOnOrOffline ff = new FriendOnOrOffline();
         ff.writeDouble((double)rid);
         ff.writeByte(type);
         ff.writeBoolean(true);
         ff.writeShort(icons[0]);
         ff.writeShort(icons[1]);
         player.writePacket(ff);
         ff.destroy();
         ff = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void friendOffline(Player player, long rid, int type) {
      try {
         FriendOnOrOffline ff = new FriendOnOrOffline();
         ff.writeDouble((double)rid);
         ff.writeByte(type);
         ff.writeBoolean(false);
         player.writePacket(ff);
         ff.destroy();
         ff = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
