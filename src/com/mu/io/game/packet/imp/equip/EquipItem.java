package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class EquipItem extends ReadAndWritePacket {
   public EquipItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      int slot = this.readByte();
      Item item = player.getBackpack().getItemByID(itemID);
      int result = player.getItemManager().equipItem(item, slot).getResult();
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }
}
