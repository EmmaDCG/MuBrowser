package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class UpdateCount extends WriteOnlyPacket {
   public UpdateCount(Item item) {
      super(20003);

      try {
         this.writeByte(item.getContainerType());
         this.writeDouble((double)item.getID());
         this.writeInt(item.getCount());
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendToClient(Player player, Item item) {
      UpdateCount uc = new UpdateCount(item);
      player.writePacket(uc);
      uc.destroy();
      uc = null;
   }
}
