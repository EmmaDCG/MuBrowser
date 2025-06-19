package com.mu.io.game.packet.imp.attack;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class FightResult extends WriteOnlyPacket {
   public FightResult(int result, int skillID) {
      super(32006);

      try {
         this.writeInt(skillID);
         this.writeBoolean(result == 1);
         if (result != 1) {
            this.writeBoolean(result == 8014);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendToClient(Player player, int result, int skillID) {
      FightResult fr = new FightResult(result, skillID);
      player.writePacket(fr);
      fr.destroy();
      fr = null;
   }
}
