package com.mu.game.model.unit.skill.skills;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.pkMode.EvilEnum;
import com.mu.game.model.unit.skill.skills.inter.SingleAttackSkill;
import java.util.List;

public abstract class AbsSingleAttackSkill extends SingleSkill implements SingleAttackSkill {
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum;

   public AbsSingleAttackSkill(int skillID, int level, Creature owner) {
      super(skillID, level, owner);
   }

   public int singleEffectedCheck(Creature effected) {
      if (effected != null && !effected.isDie() && !effected.isDestroy()) {
         if (!this.hasInEffectType(effected)) {
            return 8013;
         } else {
            switch(this.getOwner().getType()) {
            case 1:
               return this.roleSinglePreCheck(effected);
            case 2:
               return this.monsterSinglePreCheck(effected);
            case 3:
               return this.npcSinglePreCheck(effected);
            default:
               return 1;
            }
         }
      } else {
         return 8013;
      }
   }

   protected int roleSinglePreCheck(Creature effected) {
      return effected.canBeAttackedByPlayer((Player)this.getOwner());
   }

   protected int monsterSinglePreCheck(Creature effected) {
      switch(effected.getType()) {
      case 1:
      case 4:
         return 1;
      case 2:
         return 8026;
      case 3:
         return 1;
      default:
         return 1;
      }
   }

   protected int npcSinglePreCheck(Creature effected) {
      switch(effected.getType()) {
      case 1:
      case 3:
      case 4:
         return 8026;
      case 2:
         return 1;
      default:
         return 1;
      }
   }

   public void checkEvilenum(Creature effected) {
      if (this.getOwner().getType() == 1) {
         if (!this.getOwner().getMap().isPkPunishment()) {
            return;
         }

         Player player = (Player)this.getOwner();
         boolean changeEvil = false;
         switch($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum()[player.getSelfEvilEnum().ordinal()]) {
         default:
            if (!changeEvil && effected.getType() == 1) {
               Player p = (Player)effected;
               switch($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum()[p.getSelfEvilEnum().ordinal()]) {
               case 3:
               case 4:
                  break;
               default:
                  changeEvil = true;
               }
            }
         case 3:
         case 4:
            if (changeEvil) {
               this.getOwner().getBuffManager().createAndStartBuff(this.getOwner(), 80001, 1, true, 0L, (List)null);
            }
         }
      }

   }

   public void effectedBeingAttacked(Creature effected, AttackResult result) {
      effected.hpReduceForDamage(this.getOwner(), result);
   }

   public void attackTrigger(Creature effected, AttackResult result) {
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum;
      if ($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum != null) {
         return var10000;
      } else {
         int[] var0 = new int[EvilEnum.values().length];

         try {
            var0[EvilEnum.Evil_Gray.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[EvilEnum.Evil_Orange.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[EvilEnum.Evil_Red.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[EvilEnum.Evil_White.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum = var0;
         return var0;
      }
   }
}
