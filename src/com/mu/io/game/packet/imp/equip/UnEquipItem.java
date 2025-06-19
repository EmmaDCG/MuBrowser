package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class UnEquipItem extends ReadAndWritePacket {
   public UnEquipItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      int slot = this.readShort();
      int result = player.getEquipment().unEquipItem(itemID, slot, false);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }
}
