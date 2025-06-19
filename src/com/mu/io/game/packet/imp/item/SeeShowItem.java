package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ShowItemManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class SeeShowItem extends ReadAndWritePacket {
   public SeeShowItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long id = (long)this.readDouble();
      Item item = ShowItemManager.getShowItem(id);
      if (item == null) {
         SystemMessage.writeMessage(player, 3002);
      } else {
         GetItemStats.writeItem(item, this);
         player.writePacket(this);
      }
   }
}
