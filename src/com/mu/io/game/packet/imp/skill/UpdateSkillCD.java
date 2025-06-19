package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;

public class UpdateSkillCD extends WriteOnlyPacket {
   public UpdateSkillCD(HashMap skills) {
      super(30009);

      try {
         this.writeByte(skills.size());
         Iterator var3 = skills.values().iterator();

         while(var3.hasNext()) {
            Skill skill = (Skill)var3.next();
            this.writeInt(skill.getSkillID());
            this.writeInt(skill.getRemainTime());
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendToClient(Player player) {
      HashMap skillMap = player.getSkillManager().getSkillMap();
      UpdateSkillCD us = new UpdateSkillCD(skillMap);
      player.writePacket(us);
      us.destroy();
      us = null;
   }

   public static void sendToClient(Skill skill) {
      if (skill.getOwner().getType() == 1) {
         Player player = (Player)skill.getOwner();
         HashMap skills = new HashMap();
         skills.put(skill.getSkillID(), skill);
         UpdateSkillCD us = new UpdateSkillCD(skills);
         player.writePacket(us);
         us.destroy();
         us = null;
         skills.clear();
      }

   }
}
