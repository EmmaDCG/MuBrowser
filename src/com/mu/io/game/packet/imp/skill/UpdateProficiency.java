package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class UpdateProficiency extends WriteOnlyPacket {
   public UpdateProficiency(int skillID, int proficiency) {
      super(30005);

      try {
         this.writeInt(skillID);
         this.writeInt(proficiency);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendToClient(Creature owner, int skillID, int proficiency) {
      try {
         Player player = null;
         if (owner.getType() == 1) {
            player = (Player)owner;
         }

         if (player == null) {
            return;
         }

         UpdateProficiency up = new UpdateProficiency(skillID, proficiency);
         player.writePacket(up);
         up.destroy();
         up = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
