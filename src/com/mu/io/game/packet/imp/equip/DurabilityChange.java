package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class DurabilityChange extends WriteOnlyPacket {
   public DurabilityChange() {
      super(20227);
   }

   public void setData(Item item) throws Exception {
      this.writeDouble((double)item.getID());
      this.writeShort(item.getDurability());
      this.writeShort(item.getMaxDurability());
   }

   public static void sendToClient(Player player, Item item) {
      try {
         DurabilityChange dc = new DurabilityChange();
         dc.setData(item);
         player.writePacket(dc);
         dc.destroy();
         dc = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
