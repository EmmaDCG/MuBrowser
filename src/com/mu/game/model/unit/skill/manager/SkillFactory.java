package com.mu.game.model.unit.skill.manager;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.game.model.unit.skill.skills.impl.GroupAttackSkill;
import com.mu.game.model.unit.skill.skills.impl.GroupGainSkill;
import com.mu.game.model.unit.skill.skills.impl.PassiveSkill;
import com.mu.game.model.unit.skill.skills.impl.SingleAttackSkill;
import com.mu.game.model.unit.skill.skills.impl.SingleGainSkill;
import com.mu.game.model.unit.skill.skills.impl.SprintSkill;

public class SkillFactory {
   public static Skill createSkill(int skillID, int level, Creature owner) {
      Skill skill = null;
      SkillModel model = SkillModel.getModel(skillID);
      if (model == null) {
         return (Skill)skill;
      } else {
         switch(model.getType()) {
         case 1:
            if (model.getGroupType() == 1) {
               skill = new SingleAttackSkill(skillID, level, owner);
            } else {
               skill = new GroupAttackSkill(skillID, level, owner);
            }
            break;
         case 2:
            skill = new SprintSkill(skillID, level, owner);
            break;
         case 3:
            if (model.getGroupType() == 1) {
               skill = new SingleGainSkill(skillID, level, owner);
            } else {
               skill = new GroupGainSkill(skillID, level, owner);
            }
            break;
         case 4:
            skill = new PassiveSkill(skillID, level, owner);
         }

         if (skill != null) {
            ((Skill)skill).init();
         }

         return (Skill)skill;
      }
   }
}
