package com.mu.game.model.unit.skill.action.impl;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.action.SkillAction;
import com.mu.game.model.unit.skill.skills.GroupSkill;
import com.mu.game.model.unit.skill.skills.SingleSkill;
import java.awt.Point;
import java.util.HashMap;

public class S_ContinuousAttack extends SkillAction {
   private int number;
   private int interalTime;

   public S_ContinuousAttack(int type) {
      super(type);
   }

   public int preCheck(Skill skill, Creature effected, Point specifyPoint) {
      return 1;
   }

   public void initCheck(String des) throws Exception {
      if (this.interalTime < 1) {
         throw new Exception(des + "-间隔时间太短");
      } else if (this.number < 1) {
         throw new Exception(des + "-持续次数数值不正确");
      }
   }

   public void singleAction(SingleSkill skill, Creature effected, AttackResult result, Point centPoint) {
   }

   public void groupAction(GroupSkill skill, HashMap results, Point centPoint) {
   }
}
