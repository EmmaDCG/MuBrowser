package com.mu.io.game.packet.imp.friend;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class FriendWish extends ReadAndWritePacket {
   public FriendWish(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      player.getFriendManager().wish();
   }
}
