package com.mu.io.game.packet.imp.drop;

import com.mu.game.model.drop.DropItem;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class PickoutItem extends ReadAndWritePacket {
   public PickoutItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public PickoutItem(long itemID) {
      super(20301, (byte[])null);

      try {
         this.writeBoolean(itemID != -1L);
         if (itemID != -1L) {
            this.writeDouble((double)itemID);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long id = (long)this.readDouble();
      DropItem dropItem = player.getMap().getDropItem(id);
      if (dropItem == null) {
         sendToClient(player, -1L);
      } else {
         dropItem.pickout(player);
      }
   }

   public static void sendToClient(Player player, long itemID) {
      PickoutItem pi = new PickoutItem(itemID);
      player.writePacket(pi);
      pi.destroy();
      pi = null;
   }
}
