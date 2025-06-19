package com.mu.io.game.packet.imp.friend;

import com.mu.game.CenterManager;
import com.mu.game.model.friend.FriendManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class FriendBless extends ReadAndWritePacket {
   public FriendBless(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long rid = (long)this.readDouble();
      FriendManager.bless(player, CenterManager.getPlayerByRoleID(rid));
   }
}
