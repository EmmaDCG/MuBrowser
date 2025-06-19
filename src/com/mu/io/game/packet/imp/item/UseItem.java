package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class UseItem extends ReadAndWritePacket {
   public UseItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemId = (long)this.readDouble();
      int useNum = this.readInt();
      Item item = player.getBackpack().getItemByID(itemId);
      player.getItemManager().useItem(item, useNum, false).getResult();
   }
}
