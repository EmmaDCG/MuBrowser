package com.mu.io.game.packet.imp.mall;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class MallOpenLink extends ReadAndWritePacket {
   public MallOpenLink(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemId = (long)this.readDouble();
      int count = this.readInt();
      MallShowItem.show(player, itemId, count);
   }
}
