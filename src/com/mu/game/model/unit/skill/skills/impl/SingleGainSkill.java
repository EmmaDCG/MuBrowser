package com.mu.game.model.unit.skill.skills.impl;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.skill.action.SkillAction;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.game.model.unit.skill.skills.AbsSingleGainSkill;
import com.mu.io.game.packet.imp.attack.AttackCreature;
import java.awt.Point;
import java.util.HashMap;

public class SingleGainSkill extends AbsSingleGainSkill {
   public SingleGainSkill(int skillID, int level, Creature owner) {
      super(skillID, level, owner);
   }

   public int preCastCheck(Point centPoint, Creature effected) {
      if (!this.owner.isDie() && !this.owner.isDestroy()) {
         this.actualEffected = effected;
         int result = this.commonCheck();
         if (result != 1) {
            return result;
         } else {
            result = this.singlePreCheck(centPoint);
            if (result != 1) {
               return result;
            } else {
               SkillModel model = this.getModel();
               switch(model.getDatum()) {
               case 1:
                  result = AttackCreature.distanceCheck(this.actualEffected.getActualPosition(), this.owner.getActualPosition(), model.getDistance());
                  if (result != 1) {
                     return result;
                  }
               case 2:
               case 3:
               default:
                  break;
               case 4:
                  if (!this.isSelf(this.actualEffected)) {
                     result = AttackCreature.distanceCheck(this.actualEffected.getActualPosition(), this.owner.getActualPosition(), model.getDistance());
                     if (result != 1) {
                        if (this.getModel().getAction() == null || this.getModel().getAction().getType() != 3) {
                           return result;
                        }

                        this.actualEffected = this.owner;
                        this.actualPoint = this.owner.getPosition();
                        boolean var5 = true;
                     }
                  }
               }

               result = this.actionCheck(this.actualEffected, this.actualPoint);
               if (result != 1) {
                  return result;
               } else if (this.actualEffected == null) {
                  return 8026;
               } else {
                  result = this.useCondition();
                  return result != 1 ? result : 1;
               }
            }
         }
      } else {
         return 8027;
      }
   }

   public void startEffect(Point centPoint, Creature effected) {
      this.doStartEffect();
   }

   private void doStartEffect() {
      try {
         if (this.getModel().getStatusType() != 0) {
            this.singleSend((AttackResult)null, this.actualEffected, this.actualPoint);
         }

         AttackResult result = AttackResult.createNoneResult(this.actualEffected, this.getSkillID());
         SkillAction action = this.getModel().getAction();
         if (action != null) {
            action.singleAction(this, this.actualEffected, result, this.actualPoint);
         }

         if (this.isDeBenefiesSkill()) {
            HashMap results = new HashMap();
            results.put(this.actualEffected, result);
            this.getOwner().getTriggerManager().handleAttack(results);
            results.clear();
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public boolean isSprint() {
      return false;
   }
}
