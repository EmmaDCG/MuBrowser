package com.mu.io.game.packet.imp.team;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class PushTeamExpBouns extends WriteOnlyPacket {
   public PushTeamExpBouns() {
      super(11015);
   }

   public static void pushBouns(Player player) {
      try {
         PushTeamExpBouns pb = new PushTeamExpBouns();
         pb.writeShort(player.getTeamExpBonus() / 1000);
         player.writePacket(pb);
         pb.destroy();
         pb = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
