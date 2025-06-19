package com.mu.game.model.map.enter.req;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class EnterMapEquipWingRequirement implements EnterMapRequirement {
   public boolean canEnter(Player player, boolean b) {
      if (player.getEquipment().getItemBySlot(12) != null) {
         return true;
      } else {
         if (b) {
            SystemMessage.writeMessage(player, 1031);
         }

         return false;
      }
   }
}
