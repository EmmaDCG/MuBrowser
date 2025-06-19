package com.mu.io.game.packet.imp.magicItem;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class MoveMagicToBackpack extends ReadAndWritePacket {
   public MoveMagicToBackpack(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int slot = this.readShort();
      int result = 1;
      if (slot != -1) {
         Item item = player.getTreasureHouse().getItemBySlot(slot);
         if (item == null) {
            SystemMessage.writeMessage(player, 3002);
            return;
         }

         result = player.getItemManager().moveToOtherContainer(14, 1, item.getID(), -1).getResult();
      } else {
         result = player.getItemManager().moveAllMagicToBackpack().getResult();
      }

      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      } else {
         SystemMessage.writeMessage(player, 2012);
      }

   }
}
