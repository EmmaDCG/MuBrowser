package com.mu.game.model.unit.skill.damageCalculation;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackConstant;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.skill.Skill;
import com.mu.utils.Rnd;

public class SkillDamageCalculation {
   private static double getStatPercentValue(Creature creature, StatEnum stat) {
      int value = creature.getStatValue(stat);
      return stat.isPercent() ? 1.0D * (double)value / 100000.0D : (double)value;
   }

   public static AttackResult calSkillDamage(Creature attacker, Creature defencer, Skill skill) {
      long hitRnd = getHitRate(attacker, defencer);
      AttackResult result = null;
      if (hitRnd > 100000L || hitRnd >= 0L && (long)Rnd.get(100000) < hitRnd) {
         double damage = (double)defencer.getStatValue(StatEnum.DAM_FORCE);
         int damageType = 1;
         int sdReduce = 0;
         boolean isCrit = false;
         if (!isForcedInjury(attacker, defencer)) {
            int maxBasicAttakc = attacker.getMaxAtk();
            int minBasicAttack = attacker.getMinAtk();
            double coeff = (double)skill.getSkillCorrection();
            boolean excellentAttack = isExcellentAttack(attacker, defencer);
            double excelCoeff = 1.0D;
            double luckyCoeff = 1.0D;
            if (excellentAttack) {
               excelCoeff += getStatPercentValue(attacker, StatEnum.ATK_EXCELLENT_DAM);
               damageType = 3;
            } else {
               boolean luckyAttack = isLuckyAttack(attacker, defencer);
               if (luckyAttack) {
                  luckyCoeff += getStatPercentValue(attacker, StatEnum.ATK_LUCKY_DAM);
                  damageType = 4;
                  minBasicAttack = maxBasicAttakc;
               }
            }

            int def = defencer.getDef() - attacker.getStatValue(StatEnum.IGNORE_DEF);
            if (Rnd.get(100000) <= attacker.getStatValue(StatEnum.IGNORE_DEF_PRO)) {
               def = 0;
               damageType = 9;
            }

            def = Math.max(0, def);
            int attack = Rnd.get(minBasicAttack, maxBasicAttakc);
            damage = ((double)attack * luckyCoeff - (double)def * (1.0D + getStatPercentValue(defencer, StatEnum.DEF_STRENGTH))) * excelCoeff * coeff * getStatPercentValue(defencer, StatEnum.DAM_REDUCE) * getStatPercentValue(defencer, StatEnum.DAM_ABSORB);
            damage = Math.max(1.0D, damage);
            int faterRate = attacker.getStatValue(StatEnum.ATK_FATAL_RATE) - defencer.getStatValue(StatEnum.ATK_FATAL_RES);
            if (Rnd.get(100000) <= faterRate) {
               damage += damage * getStatPercentValue(attacker, StatEnum.ATK_FATAL_DAM);
               isCrit = true;
            }

            int pvpDeepen = 0;
            if (attacker.getType() == 1 && defencer.getType() == 1) {
               pvpDeepen = attacker.getStatValue(StatEnum.DAM_PVP);
            }

            damage = damage * (getStatPercentValue(attacker, StatEnum.DAM_STRENGTHEN) + getStatPercentValue(attacker, StatEnum.SKILL_ATK)) + (double)attacker.getStatValue(StatEnum.DAM_IGNORE) + (double)pvpDeepen + (double)skill.getFixedDamage();
            if (defencer.getSd() > 0 && (attacker.getType() != 2 || ((Monster)attacker).isBoss())) {
               sdReduce = Math.min(defencer.getSd(), (int)(damage * getStatPercentValue(defencer, StatEnum.SD_REDUCTION)));
            }

            damage -= (double)sdReduce;
            damage = getActualDamage(attacker, defencer, skill, damage);
            damage = Math.max(1.0D, damage);
         }

         result = new AttackResult(damageType, (int)damage, skill.getSkillID(), skill.getModel().getTargetEffect(), skill.getOwner());
         result.setSdReduce(sdReduce);
         result.setCrit(isCrit);
      } else {
         result = new AttackResult(5, 0, skill.getSkillID(), skill.getModel().getTargetEffect(), skill.getOwner());
      }

      return result;
   }

   public static double getActualDamage(Creature attacker, Creature defencer, Skill skill, double damage) {
      if (defencer.getType() == 1) {
         switch(attacker.getType()) {
         case 1:
         case 4:
            return damage * (double)skill.getModel().getAttackPlayerCoe();
         case 2:
         case 3:
         }
      }

      return damage;
   }

   public static long getHitRate(Creature attacker, Creature defencer) {
      long hit = (long)((AttackConstant.Coeff_Basic_Hit * AttackConstant.Coeff_Global + 1.0F * (float)(attacker.getAbsoluteHit() - defencer.getAbsoluteAvd()) / 100000.0F + (float)attacker.getHit() * AttackConstant.Coeff_Hit_Correction / ((float)attacker.getHit() + (float)defencer.getAvd() * AttackConstant.Coeff_Dodge_Correction)) * 100000.0F);
      return hit;
   }

   public static boolean isForcedInjury(Creature attacker, Creature defencer) {
      return defencer.getStatValue(StatEnum.DAM_FORCE) > 0;
   }

   public static boolean isExcellentAttack(Creature attacker, Creature defencer) {
      int excellentRate = attacker.getStatValue(StatEnum.ATK_EXCELLENT_RATE);
      if (excellentRate <= 0) {
         return false;
      } else {
         int excellentResRate = defencer.getStatValue(StatEnum.ATK_EXCELLENT_RES);
         int rate = excellentRate - excellentResRate;
         return Rnd.get(100000) <= rate;
      }
   }

   public static boolean isLuckyAttack(Creature attacker, Creature defencer) {
      int luckyRate = attacker.getStatValue(StatEnum.ATK_LUCKY_RATE);
      if (luckyRate <= 0) {
         return false;
      } else {
         int luckyResRate = defencer.getStatValue(StatEnum.ATK_LUCKY_RES);
         int rate = luckyRate - luckyResRate;
         return Rnd.get(100000) <= rate;
      }
   }

   public static double getFluctuation(Creature attacker) {
      return 1.0D;
   }
}
