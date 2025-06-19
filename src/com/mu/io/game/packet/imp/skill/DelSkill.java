package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class DelSkill extends WriteOnlyPacket {
   public DelSkill() {
      super(30002);
   }

   public static void sendToClient(Player player, int skillId) {
      try {
         DelSkill ds = new DelSkill();
         ds.writeByte(1);
         ds.writeInt(skillId);
         player.writePacket(ds);
         ds.destroy();
         ds = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
