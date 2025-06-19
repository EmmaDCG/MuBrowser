package com.mu.game.model.unit.skill.skills;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.pkMode.EvilEnum;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.game.model.unit.skill.seleteTarget.SkillTargetSeleteManager;
import com.mu.game.model.unit.skill.skills.inter.GroupAttackSkill;
import com.mu.io.game.packet.imp.attack.AttackCreature;
import com.mu.utils.Rnd;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public abstract class AbsGroupAttackSkill extends GroupSkill implements GroupAttackSkill {
   protected Creature actualEffected = null;
   protected Point centerPoint = null;
   protected List effectedList = null;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum;

   public AbsGroupAttackSkill(int skillID, int level, Creature owner) {
      super(skillID, level, owner);
   }

   public void clear() {
      this.actualEffected = null;
      this.centerPoint = null;
      if (this.effectedList != null) {
         this.effectedList.clear();
      }

      this.effectedList = null;
   }

   public static int singleEffectedCheck(Skill skill, Point centerPoint, Creature effected) {
      if (effected != null && !effected.isDie() && !effected.isDestroy() && effected.isCanBeAttacked()) {
         int result;
         Creature owner = skill.getOwner();
         result = 1;
         label29:
         switch(owner.getType()) {
         case 1:
            result = effected.canBeAttackedByPlayer((Player)owner);
            break;
         case 2:
            switch(effected.getType()) {
            case 2:
               result = 8026;
               break label29;
            case 3:
               result = 1;
            default:
               break label29;
            }
         case 3:
            switch(effected.getType()) {
            case 2:
               break;
            default:
               result = 8026;
            }
         }

         if (result != 1) {
            return result;
         } else {
            SkillModel model = skill.getModel();
            result = AttackCreature.distanceCheck(centerPoint, effected.getActualPosition(), model.getRange());
            return result;
         }
      } else {
         return 8013;
      }
   }

   public int datumSelfPreCheck(Creature creature) {
      this.centerPoint = this.getOwner().getActualPosition();
      int result = 1;
      if (creature != null) {
         result = singleEffectedCheck(this, this.centerPoint, creature);
         if (result == 1) {
            this.actualEffected = creature;
         }
      }

      return result;
   }

   public int datumTargetPreCheck(Creature creature) {
      if (creature != null && !creature.isDestroy()) {
         this.centerPoint = creature.getActualPosition();
         Point oPoint = this.getOwner().getActualPosition();
         int result = AttackCreature.distanceCheck(this.centerPoint, oPoint, this.getModel().getDistance());
         if (result != 1) {
            return result;
         } else {
            result = singleEffectedCheck(this, this.centerPoint, creature);
            if (result != 1) {
               return result;
            } else {
               this.actualEffected = creature;
               return 1;
            }
         }
      } else {
         return 8026;
      }
   }

   public int datumMousePreCheck(Point centPoint, Creature creature) {
      if (centPoint == null) {
         this.centerPoint = this.owner.getActualPosition();
      } else {
         this.centerPoint = centPoint;
      }

      int result = AttackCreature.distanceCheck(this.centerPoint, this.owner.getActualPosition(), this.getModel().getDistance());
      if (result != 1) {
         int allDistance = MathUtil.getDistance(this.owner.getActualPosition(), this.centerPoint);
         double rate = 1.0D * (double)this.getModel().getDistance() / (double)allDistance;
         int x = (int)((double)this.owner.getX() + (double)(this.centerPoint.x - this.owner.getX()) * rate);
         int y = (int)((double)this.owner.getY() + (double)(this.centerPoint.y - this.owner.getY()) * rate);
         this.centerPoint.x = x;
         this.centerPoint.y = y;
      }

      if (creature != null) {
         result = singleEffectedCheck(this, this.centerPoint, creature);
         if (result == 1) {
            this.actualEffected = creature;
         }
      }

      return 1;
   }

   public void effectedBeingAttacked(HashMap results) {
      Iterator var3 = results.entrySet().iterator();

      while(var3.hasNext()) {
         Entry entry = (Entry)var3.next();
         Creature creature = (Creature)entry.getKey();
         AttackResult result = (AttackResult)entry.getValue();
         creature.hpReduceForDamage(this.getOwner(), result);
         if (creature.isDie()) {
            this.effectedList.remove(creature);
         }
      }

      results = null;
   }

   public void attackTrigger(HashMap results) {
   }

   public void checkEvilEnum() {
      this.calAllEfffectedList();
      if (this.getOwner().getType() == 1) {
         if (!this.getOwner().getMap().isPkPunishment()) {
            return;
         }

         Player player = (Player)this.getOwner();
         boolean changeEvil = false;
         switch($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum()[player.getSelfEvilEnum().ordinal()]) {
         case 3:
         case 4:
            break;
         default:
            if (!changeEvil) {
               Iterator var4 = this.effectedList.iterator();

               while(var4.hasNext()) {
                  Creature ee = (Creature)var4.next();
                  if (ee.getType() == 1) {
                     Player p = (Player)ee;
                     switch($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$EvilEnum()[p.getSelfEvilEnum().ordinal()]) {
                     case 3:
                     case 4:
                        break;
                     default:
                        changeEvil = true;
                     }
                  }

                  if (changeEvil) {
                     break;
                  }
               }
            }
         }

         if (changeEvil) {
            this.getOwner().getBuffManager().createAndStartBuff(this.getOwner(), 80001, 1, true, 0L, (List)null);
         }
      }

   }

   public void checkTarget(Shape shape) {
      if (this.getOwner().getUnitType() == 1) {
         switch(this.getModel().getDatum()) {
         case 3:
            if (this.actualEffected != null && !shape.contains(this.actualEffected.getActualPosition())) {
               this.actualEffected = null;
            }
         default:
         }
      }
   }

   public void calAllEfffectedList() {
      this.effectedList = SkillTargetSeleteManager.getEffectList(this, this.centerPoint);
      if (this.actualEffected != null) {
         if (this.effectedList != null && this.effectedList.size() > 0) {
            boolean hasContain = this.effectedList.indexOf(this.actualEffected) != -1;
            if (!hasContain) {
               if (this.effectedList.size() >= this.getMaxCount()) {
                  this.effectedList.remove(Rnd.get(this.effectedList.size()));
               }

               this.effectedList.add(this.actualEffected);
            }
         } else {
            if (this.effectedList == null) {
               this.effectedList = new ArrayList();
            }

            this.effectedList.add(this.actualEffected);
         }
      }

   }

   public void stop() {
      super.stop();
      if (this.effectedList != null) {
         this.effectedList.clear();
      }

      this.effectedList = null;
      this.actualEffected = null;
      this.centerPoint = null;
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
