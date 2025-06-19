package com.mu.io.game.packet.imp.friend;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class DeleteFriend extends ReadAndWritePacket {
   public DeleteFriend(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public DeleteFriend() {
      super(11205, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long rid = (long)this.readDouble();
      int type = this.readByte();
      player.getFriendManager().deleteFriend(rid, type);
   }

   public static void deleteSuccess(Player player, long rid, int type) {
      try {
         DeleteFriend df = new DeleteFriend();
         df.writeDouble((double)rid);
         df.writeByte(type);
         player.writePacket(df);
         df.destroy();
         df = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
