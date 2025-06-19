package com.mu.game.model.unit.skill.skills.impl;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.skill.action.SkillAction;
import com.mu.game.model.unit.skill.damageCalculation.SkillDamageCalculation;
import com.mu.game.model.unit.skill.skills.AbsGroupAttackSkill;
import java.awt.Point;
import java.util.HashMap;

public class GroupAttackSkill extends AbsGroupAttackSkill {
   public GroupAttackSkill(int skillID, int level, Creature owner) {
      super(skillID, level, owner);
   }

   public int preCastCheck(Point centPoint, Creature effected) {
      int result = 1;
      if (!this.owner.isDie() && !this.owner.isDestroy()) {
         result = this.commonCheck();
         if (result != 1) {
            return result;
         } else {
            result = this.useCondition();
            if (result != 1) {
               return result;
            } else {
               this.clear();
               switch(this.getModel().getDatum()) {
               case 1:
                  result = this.datumTargetPreCheck(effected);
                  break;
               case 2:
                  result = this.datumSelfPreCheck(effected);
                  break;
               default:
                  result = this.datumMousePreCheck(centPoint, effected);
               }

               return result;
            }
         }
      } else {
         return 8027;
      }
   }

   protected void startEffect(Point centPoint, Creature effected) {
      this.doStartEffect();
   }

   public void doStartEffect() {
      this.checkEvilEnum();
      if (this.effectedList.size() == 0) {
         this.groupAttackSend((HashMap)null, this.centerPoint, this.actualEffected);
      } else {
         HashMap results = new HashMap();
         this.calDamage(results);
         if (results.size() > 0) {
            this.effectedBeingAttacked(results);
            this.attackTrigger(results);
         }

         this.groupAttackSend(results, this.centerPoint, this.actualEffected);
         SkillAction action = this.getModel().getAction();
         if (action != null) {
            action.groupAction(this, results, this.centerPoint);
         }

         this.getOwner().getTriggerManager().handleAttack(results);
         results.clear();
      }
   }

   protected void calDamage(HashMap results) {
      for(int i = 0; i < this.effectedList.size(); ++i) {
         Creature e = (Creature)this.effectedList.get(i);
         AttackResult result = SkillDamageCalculation.calSkillDamage(this.getOwner(), e, this);
         boolean success = e.beAttacked(this.owner, result);
         this.changCreateType(result);
         if (success) {
            results.put(e, result);
         }
      }

   }

   public void changCreateType(AttackResult result) {
   }

   public boolean isSprint() {
      return false;
   }
}
