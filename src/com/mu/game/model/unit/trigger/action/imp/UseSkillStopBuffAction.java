package com.mu.game.model.unit.trigger.action.imp;

import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.trigger.action.AbsUseSkillAction;

public class UseSkillStopBuffAction extends AbsUseSkillAction {
   private int buffID;

   public UseSkillStopBuffAction(int id, int buffID) {
      super(id);
      this.buffID = buffID;
   }

   public void useSkill(Skill skill) {
      if (skill != null) {
         if (!skill.getModel().isPassive()) {
            this.getOwner().getBuffManager().endBuff(this.buffID, true);
         }
      }
   }

   public boolean privyCondition() {
      return true;
   }

   public int getBuffID() {
      return this.buffID;
   }
}
