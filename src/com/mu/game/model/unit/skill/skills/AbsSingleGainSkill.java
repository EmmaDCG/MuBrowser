package com.mu.game.model.unit.skill.skills;

import com.mu.game.model.gang.GangManager;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.team.TeamManager;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.pkMode.PkEnum;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.game.model.unit.skill.skills.inter.SingleGainSkill;
import java.awt.Point;

public abstract class AbsSingleGainSkill extends SingleSkill implements SingleGainSkill {
   protected Creature actualEffected = null;
   protected Point actualPoint = null;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum;

   public AbsSingleGainSkill(int skillID, int level, Creature owner) {
      super(skillID, level, owner);
   }

   public int singlePreCheck(Point centPoint) {
      boolean isInparty = false;
      SkillModel model = this.getModel();
      switch(model.getEffectedObject()) {
      case 1:
         this.actualEffected = this.owner;
         break;
      case 2:
         if (this.actualEffected == null) {
            return 8026;
         }

         if (inSameParty(this.getOwner(), this.actualEffected)) {
            return 8026;
         }
         break;
      case 3:
      default:
         return 8026;
      case 4:
         if (this.actualEffected == null) {
            this.actualEffected = this.owner;
         } else if (!this.isSelf(this.actualEffected)) {
            isInparty = inSameParty(this.getOwner(), this.actualEffected);
            if (!isInparty) {
               if (!this.isSpecialDatum()) {
                  return 8013;
               }

               this.actualEffected = this.owner;
            }

            if (!this.hasInEffectType(this.actualEffected)) {
               return 8013;
            }
         }
         break;
      case 5:
         if (this.actualEffected == null) {
            return 8026;
         }

         if (this.isSelf(this.actualEffected)) {
            return 8013;
         }

         isInparty = inSameParty(this.getOwner(), this.actualEffected);
         if (!isInparty) {
            return 8013;
         }

         if (!this.hasInEffectType(this.actualEffected)) {
            return 8013;
         }
         break;
      case 6:
         if (this.getOwner().getType() != 4) {
            return 8026;
         }

         this.actualEffected = ((Pet)this.getOwner()).getOwner();
         break;
      case 7:
         if (this.getOwner().getType() != 4) {
            return 8026;
         }

         if (this.isSelf(this.actualEffected)) {
            return 1;
         }

         return 8013;
      }

      switch(model.getDatum()) {
      case 3:
         if (centPoint == null) {
            this.actualPoint = this.actualEffected.getActualPosition();
         } else {
            this.actualPoint = centPoint;
         }
         break;
      default:
         this.actualPoint = this.actualEffected.getActualPosition();
      }

      return 1;
   }

   private boolean isSpecialDatum() {
      return this.getModel().getDatum() == 4 && !this.isDeBenefiesSkill();
   }

   protected boolean isSelf(Creature effected) {
      return this.getOwner().getType() == effected.getType() && this.getOwner().getID() == effected.getID();
   }

   public static boolean inSameParty(Creature creature, Creature effected) {
      switch(creature.getType()) {
      case 1:
         return inRoleSameParty((Player)creature, effected);
      case 2:
         return isMonsterSameParty((Monster)creature, effected);
      case 3:
      default:
         return false;
      case 4:
         return inRoleSameParty(((Pet)creature).getOwner(), effected);
      }
   }

   public static boolean inRoleSameParty(Player player, Creature effected) {
      Player other = null;
      switch(effected.getType()) {
      case 1:
         other = (Player)effected;
      case 2:
      case 3:
      default:
         break;
      case 4:
         other = ((Pet)effected).getOwner();
      }

      if (other != null) {
         switch($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum()[player.getPkMode().getCurrentPKMode().ordinal()]) {
         case 1:
         case 2:
            if (TeamManager.isTeammate(player, other)) {
               return true;
            }

            if (GangManager.inSameGang(player.getID(), other.getID())) {
               return true;
            }
         }
      }

      return false;
   }

   private static boolean isMonsterSameParty(Monster monster, Creature effected) {
      switch(effected.getType()) {
      case 2:
         return true;
      default:
         return false;
      }
   }

   public void stop() {
      this.actualEffected = null;
      super.stop();
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum;
      if ($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum != null) {
         return var10000;
      } else {
         int[] var0 = new int[PkEnum.values().length];

         try {
            var0[PkEnum.Mode_Force.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[PkEnum.Mode_Massacre.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[PkEnum.Mode_Peace.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum = var0;
         return var0;
      }
   }
}
