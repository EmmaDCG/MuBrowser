package com.mu.game.model.unit.skill.skills;

import com.mu.game.model.pet.Pet;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.game.model.unit.skill.skills.inter.GroupGainSkill;
import com.mu.io.game.packet.imp.attack.AttackCreature;
import java.awt.Point;
import java.util.List;

public abstract class AbsGroupGainSkill extends GroupSkill implements GroupGainSkill {
   protected Creature actualEffected = null;
   protected Point actualPoint = null;
   protected List actualEffectedList = null;

   public AbsGroupGainSkill(int skillID, int level, Creature owner) {
      super(skillID, level, owner);
   }

   protected int singleCheck(Creature effected, Point _actualPoint) {
      SkillModel model = this.getModel();
      switch(model.getEffectedObject()) {
      case 2:
         if (!this.hasInEffectType(effected)) {
            return 8013;
         }

         if (this.isSelf(effected) || AbsGroupAttackSkill.singleEffectedCheck(this, _actualPoint, effected) != 1) {
            return 8013;
         }
         break;
      case 3:
      case 6:
      default:
         return 8026;
      case 4:
         if (this.isSelf(effected)) {
            return 1;
         }

         if (!AbsSingleGainSkill.inSameParty(this.getOwner(), effected)) {
            return 8013;
         }

         if (!this.hasInEffectType(effected)) {
            return 8013;
         }
         break;
      case 5:
         if (this.isSelf(effected)) {
            return 8013;
         }

         if (AbsSingleGainSkill.inSameParty(this.getOwner(), effected)) {
            return 8013;
         }

         if (!this.hasInEffectType(effected)) {
            return 8013;
         }
         break;
      case 7:
         if (this.getOwner().getType() != 4) {
            return 8026;
         }

         if (this.isSelf(effected)) {
            return 1;
         }

         if (effected.getType() != 1 || ((Pet)this.getOwner()).getOwner().getID() != effected.getID()) {
            return 8013;
         }
      }

      return 1;
   }

   public boolean isSelf(Creature effected) {
      return this.getOwner().getType() == effected.getType() && this.getOwner().getID() == effected.getID();
   }

   public int datumSelfPreCheck() {
      this.actualPoint = this.getOwner().getActualPosition();
      if (this.singleCheck(this.getOwner(), this.actualPoint) == 1) {
         this.actualEffected = this.getOwner();
      }

      return 1;
   }

   public int datumTargetPreCheck(Creature creature) {
      if (creature == null) {
         return 8026;
      } else if (this.singleCheck(creature, creature.getActualPosition()) == 1) {
         this.actualEffected = creature;
         this.actualPoint = creature.getActualPosition();
         int result = AttackCreature.distanceCheck(this.getOwner().getActualPosition(), this.actualPoint, this.getModel().getDistance());
         return result != 1 ? result : 1;
      } else {
         return 8026;
      }
   }

   public int datumMousePreCheck(Point centPoint) {
      if (centPoint == null) {
         this.actualPoint = this.owner.getActualPosition();
      } else {
         this.actualPoint = centPoint;
      }

      int result = AttackCreature.distanceCheck(this.actualPoint, this.owner.getActualPosition(), this.getModel().getDistance());
      return result != 1 ? result : 1;
   }

   public boolean isSprint() {
      return false;
   }

   public void clear() {
      this.actualEffected = null;
      this.actualEffected = null;
      if (this.actualEffectedList != null) {
         this.actualEffectedList.clear();
      }

      this.actualEffectedList = null;
   }
}
