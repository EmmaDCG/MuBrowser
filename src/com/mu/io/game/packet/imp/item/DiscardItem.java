package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class DiscardItem extends ReadAndWritePacket {
   public DiscardItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      Item item = player.getBackpack().getItemByID(itemID);
      OperationResult result = player.getItemManager().deleteItem(item, 14);
      this.writeBoolean(result.isOk());
      player.writePacket(this);
   }
}
