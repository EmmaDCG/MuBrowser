package com.mu.game.model.activity.imp.test;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ActivityTestElement extends ActivityElement {
   public ActivityTestElement(int id, ActivityTest father) {
      super(id, father);
   }

   public int getReceiveType() {
      return 4;
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
   }

   public boolean canReceive(Player player, boolean notice) {
      if (!((ActivityTest)this.getFather()).isOpen()) {
         return false;
      } else if (player.getLevel() < 100) {
         return false;
      } else {
         return !this.receiveOverload(player);
      }
   }

   public int getReceiveStatus(Player player) {
      if (player.getLevel() < 100) {
         return 0;
      } else {
         return this.receiveOverload(player) ? 2 : 1;
      }
   }
}
