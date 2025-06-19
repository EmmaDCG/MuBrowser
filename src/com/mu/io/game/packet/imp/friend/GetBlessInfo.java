package com.mu.io.game.packet.imp.friend;

import com.mu.game.model.friend.FriendBlessInfo;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class GetBlessInfo extends ReadAndWritePacket {
   public GetBlessInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public GetBlessInfo() {
      super(11216, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      writeInfo(player);
   }

   public static void writeOneInfo(Player player, FriendBlessInfo info) {
      try {
         GetBlessInfo gi = new GetBlessInfo();
         gi.writeByte(1);
         gi.writeDouble((double)info.getTime());
         gi.writeDouble((double)info.getId());
         gi.writeUTF(info.getName());
         player.writePacket(gi);
         gi.destroy();
         gi = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void writeInfo(Player player) {
      try {
         ConcurrentHashMap map = player.getFriendManager().getBlessMap();
         GetBlessInfo gi = new GetBlessInfo();
         gi.writeByte(map.size());
         Iterator it = map.values().iterator();

         while(it.hasNext()) {
            FriendBlessInfo info = (FriendBlessInfo)it.next();
            gi.writeDouble((double)info.getTime());
            gi.writeDouble((double)info.getId());
            gi.writeUTF(info.getName());
         }

         player.writePacket(gi);
         gi.destroy();
         gi = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
