package com.mu.game.model.unit.skill.skills.impl;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.skill.action.SkillAction;
import com.mu.game.model.unit.skill.damageCalculation.SkillDamageCalculation;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.game.model.unit.skill.skills.AbsSingleAttackSkill;
import com.mu.io.game.packet.imp.attack.AttackCreature;
import java.awt.Point;
import java.util.HashMap;

public class SingleAttackSkill extends AbsSingleAttackSkill {
   public SingleAttackSkill(int skillID, int level, Creature owner) {
      super(skillID, level, owner);
   }

   public int preCastCheck(Point centPoint, Creature effected) {
      if (!this.owner.isDie() && !this.owner.isDestroy()) {
         int result = this.commonCheck();
         if (result != 1) {
            return result;
         } else {
            result = this.singleEffectedCheck(effected);
            if (result != 1) {
               return result;
            } else {
               SkillModel model = this.getModel();
               result = AttackCreature.distanceCheck(effected.getActualPosition(), this.owner.getActualPosition(), model.getDistance());
               if (result != 1) {
                  return result;
               } else {
                  result = this.useCondition();
                  return result != 1 ? result : result;
               }
            }
         }
      } else {
         return 8027;
      }
   }

   protected void startEffect(Point centPoint, Creature effected) {
      this.checkEvilenum(effected);
      AttackResult result = SkillDamageCalculation.calSkillDamage(this.getOwner(), effected, this);
      boolean success = effected.beAttacked(this.getOwner(), result);
      if (success) {
         effected.hpReduceForDamage(this.getOwner(), result);
      }

      this.doSendAndAction(result, effected, centPoint);
      HashMap results = this.createResult(effected, result);
      this.getOwner().getTriggerManager().handleAttack(results);
      results.clear();
   }

   protected void doSendAndAction(AttackResult result, Creature effected, Point centPoint) {
      this.singleSend(result, effected, centPoint);
      if (!effected.isDie()) {
         SkillAction action = this.getModel().getAction();
         if (action != null) {
            action.singleAction(this, effected, result, centPoint);
         }
      }

   }

   public boolean isSprint() {
      return false;
   }
}
