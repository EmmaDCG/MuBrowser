package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.game.top.TopManager;
import com.mu.io.game.packet.ReadAndWritePacket;

public class GetTopList extends ReadAndWritePacket {
   public GetTopList(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int type = this.readByte();
      int page = this.readByte();
      Player player = this.getPlayer();
      TopManager.writeTopList(type, page, player, this);
   }
}
