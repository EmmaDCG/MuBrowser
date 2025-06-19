package com.mu.io.game.packet.imp.friend;

import com.mu.config.MessageText;
import com.mu.game.model.friend.FriendManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class GetWishPoolInfo extends ReadAndWritePacket {
   public GetWishPoolInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public GetWishPoolInfo() {
      super(11213, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      writeWishPoolInfo(player);
   }

   public static void writeWishPoolInfo(Player player) {
      try {
         GetWishPoolInfo packet = new GetWishPoolInfo();
         FriendManager manager = player.getFriendManager();
         packet.writeByte(FriendManager.getCanBlessTimes() - manager.getBlessTimes());
         packet.writeByte(manager.getLucky());
         packet.writeByte(FriendManager.getCanBeBlessTimes());
         int canWish = manager.canWish();
         packet.writeBoolean(canWish == 1);
         if (canWish == 1) {
            packet.writeUTF(manager.getCanWishDes());
         } else {
            packet.writeUTF(MessageText.getText(canWish));
         }

         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
