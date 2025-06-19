package com.mu.game.model.unit.skill.learnCondition;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.SkillManager;
import java.util.HashMap;
import java.util.Iterator;

public class FrontSkillCodition implements LearnCondition {
   private HashMap frontSkills = null;

   public FrontSkillCodition(HashMap frontSkills) {
      this.frontSkills = frontSkills;
   }

   public int verify(Player player) {
      SkillManager skillManager = player.getSkillManager();
      Iterator var4 = this.frontSkills.keySet().iterator();

      while(var4.hasNext()) {
         int skillID = ((Integer)var4.next()).intValue();
         int level = ((Integer)this.frontSkills.get(skillID)).intValue();
         if (skillManager.getSkillLevel(skillID) < level) {
            return 8004;
         }
      }

      return 1;
   }

   public int getType() {
      return 3;
   }
}
