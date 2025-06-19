package com.mu.game.model.unit.skill.skills.impl;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.action.SkillAction;
import com.mu.game.model.unit.skill.seleteTarget.SkillTargetSeleteManager;
import com.mu.game.model.unit.skill.skills.AbsGroupGainSkill;
import java.awt.Point;
import java.util.HashMap;

public class GroupGainSkill extends AbsGroupGainSkill {
   public GroupGainSkill(int skillID, int level, Creature owner) {
      super(skillID, level, owner);
   }

   public int preCastCheck(Point centPoint, Creature effected) {
      if (!this.owner.isDie() && !this.owner.isDestroy()) {
         int result = this.commonCheck();
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
                  result = this.datumSelfPreCheck();
                  break;
               default:
                  result = this.datumMousePreCheck(centPoint);
               }

               if (result != 1) {
                  return result;
               } else {
                  SkillAction action = this.getModel().getAction();
                  if (action != null) {
                     result = action.preCheck(this, this.actualEffected, this.actualPoint);
                  }

                  return result;
               }
            }
         }
      } else {
         return 8027;
      }
   }

   public boolean isRobot(Creature creature) {
      if (creature.getType() != 1) {
         return false;
      } else {
         return ((Player)creature).isRobot();
      }
   }

   public void startEffect(Point centPoint, Creature creature) {
      this.actualEffectedList = SkillTargetSeleteManager.getEffectList(this, this.actualPoint);
      if (this.actualEffected != null) {
         this.actualEffectedList.remove(this.actualEffected);
         this.actualEffectedList.add(this.actualEffected);
      }

      if (this.actualEffectedList.size() == 0) {
         this.groupAttackSend((HashMap)null, this.actualPoint, this.actualEffected);
      } else {
         try {
            HashMap results = this.createResultSet(this.actualEffectedList);
            this.groupAttackSend(results, this.actualPoint, this.actualEffected);
            SkillAction action = this.getModel().getAction();
            if (action != null) {
               action.groupAction(this, results, this.actualPoint);
            }

            if (this.isDeBenefiesSkill()) {
               this.getOwner().getTriggerManager().handleAttack(results);
            }

            results.clear();
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      }
   }

   public void stop() {
      this.actualEffected = null;
      this.actualPoint = null;
      if (this.actualEffectedList != null) {
         this.actualEffectedList.clear();
      }

      this.actualEffectedList = null;
      super.stop();
   }
}
