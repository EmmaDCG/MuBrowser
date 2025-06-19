package com.mu.game.model.unit.trigger.action;

import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.trigger.inter.UseSkill;

public abstract class AbsUseSkillAction extends TriggerAction implements UseSkill {
   public AbsUseSkillAction(int id) {
      super(id);
   }

   public void handle(boolean checked, Object... objects) throws Exception {
      Skill skill = (Skill)objects[0];
      if (checked) {
         this.useSkill(skill);
      } else if (this.meedCondition()) {
         this.useSkill(skill);
      }

   }

   public int getType() {
      return 7;
   }
}
