package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.io.game.packet.WriteOnlyPacket;

public class UpdateSkill extends WriteOnlyPacket {
   public UpdateSkill() {
      super(30003);
   }

   public static void sendToClient(Player player, Skill skill) {
      try {
         UpdateSkill us = new UpdateSkill();
         us.writeInt(skill.getSkillID());
         AddSkill.writeSkillDetail(skill, us);
         player.writePacket(us);
         us.destroy();
         us = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
