package com.mu.io.game.packet.imp.item;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class SplitItem extends ReadAndWritePacket {
   public SplitItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int containerType = this.readByte();
      long itemID = (long)this.readDouble();
      int count = this.readInt();
      player.getItemManager().splitItem(itemID, count, containerType);
   }
}
