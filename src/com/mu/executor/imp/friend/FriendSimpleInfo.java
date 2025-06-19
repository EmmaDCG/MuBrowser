package com.mu.executor.imp.friend;

import com.mu.game.model.friend.Friend;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class FriendSimpleInfo extends WriteOnlyPacket {
   public FriendSimpleInfo() {
      super(11201);
   }

   public static void pushSimple(Player player) {
      try {
         FriendSimpleInfo fi = new FriendSimpleInfo();
         ArrayList list = player.getFriendManager().getAllFriend();
         fi.writeByte(list.size());
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            Friend f = (Friend)var4.next();
            fi.writeDouble((double)f.getId());
            fi.writeByte(f.getType());
         }

         player.writePacket(fi);
         fi.destroy();
         fi = null;
         list.clear();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
