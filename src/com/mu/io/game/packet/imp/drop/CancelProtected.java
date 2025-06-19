package com.mu.io.game.packet.imp.drop;

import com.mu.game.model.drop.DropItem;
import com.mu.io.game.packet.WriteOnlyPacket;

public class CancelProtected extends WriteOnlyPacket {
   public CancelProtected() {
      super(20302);
   }

   public CancelProtected(DropItem item) {
      super(20302);

      try {
         this.writeDouble((double)item.getID());
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendToClient(DropItem item) {
      if (item.getMap() != null) {
         CancelProtected cp = new CancelProtected(item);
         item.getMap().sendPacketToAroundPlayer(cp, item.getPosition());
         cp.destroy();
         cp = null;
      }
   }
}
