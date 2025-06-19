package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class SprintEnd extends WriteOnlyPacket {
   public SprintEnd() {
      super(30011);
   }

   public static void sendToClient(Creature creature) {
      if (creature.getType() == 1) {
         try {
            SprintEnd se = new SprintEnd();
            se.writeBoolean(true);
            ((Player)creature).writePacket(se);
            se.destroy();
            se = null;
         } catch (Exception var2) {
            var2.printStackTrace();
         }

      }
   }
}
