package com.mu.game.model.activity.imp.collection;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class CollectionElement extends ActivityElement {
   public CollectionElement(int id, CollectionActivity father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
   }

   public int getReceiveStatus(Player player) {
      if (this.receiveOverload(player)) {
         return 2;
      } else {
         return !((CollectionActivity)this.getFather()).isOpen() ? 0 : 1;
      }
   }
}
