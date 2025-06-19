package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class DurabilityPrompt extends WriteOnlyPacket {
   public DurabilityPrompt() {
      super(20231);
   }

   public static void sendToClient(Player player, boolean show) {
      try {
         DurabilityPrompt dp = new DurabilityPrompt();
         dp.writeBoolean(show);
         player.writePacket(dp);
         dp.destroy();
         dp = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendWhenFirstEnter(Player player) {
      if (player.getEquipment().needToSendDurability()) {
         sendToClient(player, true);
      }

   }
}
