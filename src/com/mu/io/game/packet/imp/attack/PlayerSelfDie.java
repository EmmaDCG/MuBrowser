package com.mu.io.game.packet.imp.attack;

import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class PlayerSelfDie extends WriteOnlyPacket {
   public PlayerSelfDie(Player player, AttackResult result) {
      super(32005);

      try {
         this.writeBoolean(true);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendToClient(Player player, AttackResult result) {
      PlayerSelfDie psd = new PlayerSelfDie(player, result);
      player.writePacket(psd);
      psd.destroy();
      psd = null;
   }
}
