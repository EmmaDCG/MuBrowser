package com.mu.io.game.packet.imp.friend;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class FriendAgreeOrRefuseApply extends ReadAndWritePacket {
   public FriendAgreeOrRefuseApply(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public FriendAgreeOrRefuseApply() {
      super(11207, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long rid = (long)this.readDouble();
      boolean isAgree = this.readBoolean();
      if (isAgree) {
         player.getFriendManager().agreeApply(rid);
      } else {
         player.getFriendManager().refuseApply(rid);
      }

   }

   public static void writeResult(Player player, long rid) {
      FriendAgreeOrRefuseApply ff = new FriendAgreeOrRefuseApply();

      try {
         ff.writeLong(rid);
         player.writePacket(ff);
         ff.destroy();
         ff = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
