package com.mu.game.model.unit.skill.seleteTarget;

import com.mu.game.model.pet.Pet;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.utils.Rnd;
import com.mu.utils.geom.ShapeUtil;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkillTargetSeleteManager {
   private static Logger logger = LoggerFactory.getLogger(SkillTargetSeleteManager.class);

   public static List getEffectList(Skill skill, Point cenPoint) {
      List effectedList = null;
      if (skill.isDeBenefiesSkill()) {
         effectedList = getAttackList(skill, cenPoint);
      } else {
         effectedList = getFriendlyTargets(skill, cenPoint);
      }

      return effectedList;
   }

   public static List getAttackList(Skill skill, Point cenPoint) {
      Creature owner = skill.getOwner();
      Shape shape = getArea(skill, cenPoint);
      skill.checkTarget(shape);
      List allTargets = new ArrayList();
      getTargetFromAggro(allTargets, owner, shape);
      int maxCount = skill.getMaxCount();
      if (allTargets.size() > maxCount) {
         for(int count = allTargets.size() - maxCount; count > 0; --count) {
            int index = Rnd.get(0, allTargets.size() - 1);
            allTargets.remove(index);
         }
      }

      if (allTargets.size() >= maxCount) {
         return allTargets;
      } else {
         List creatures = null;
         switch(owner.getType()) {
         case 1:
         case 4:
            creatures = RoleTargetSelectedManager.getAttackTargets(skill, cenPoint, shape);
            break;
         case 2:
         case 3:
            creatures = OtherTargetSeletedManager.getAttackTargets(skill, cenPoint, shape);
         }

         if (creatures != null && creatures.size() > 0) {
            chooseRandomTargetFilterHas(creatures, allTargets, maxCount - allTargets.size(), owner, false);
            creatures.clear();
            creatures = null;
         }

         return allTargets;
      }
   }

   public static Shape getArea(Skill skill, Point targetPoint) {
      SkillModel model = skill.getModel();
      Point ownerPoint = skill.getOwner().getActualPosition();
      switch(skill.getModel().getRangeType()) {
      case 2:
         return ShapeUtil.createLineShape(ownerPoint, targetPoint, (double)model.getRange(), (double)model.getAngle());
      case 3:
      default:
         Point startPoint = ownerPoint;
         switch(model.getDatum()) {
         case 1:
         case 3:
            startPoint = targetPoint;
         case 2:
         default:
            return ShapeUtil.createRoundShape(startPoint, model.getRange());
         }
      case 4:
         return ShapeUtil.createSectorShape(ownerPoint, targetPoint, model.getRange(), model.getAngle());
      }
   }

   protected static void getTargetFromAggro(List effectedList, Creature owner, Shape shape) {
      List aggroList = owner.getAggroList().getAggroCreatureList();
      Iterator var5 = aggroList.iterator();

      while(var5.hasNext()) {
         Creature creature = (Creature)var5.next();
         if (!creature.isDie() && !creature.isDestroy() && creature.isCanBeAttacked()) {
            if (creature.getMap() == null) {
               String des = "";
               String defenerPreStr = "";
               if (creature.getType() == 4) {
                  Pet pet = (Pet)creature;
                  defenerPreStr = pet.getName() + " 的宠物 ：";
                  des = "--宠物是否出现 " + pet.getOwner().getPetManager().isShow();
               }

               String preStr = "";
               if (owner.getType() == 4) {
                  preStr = ((Pet)owner).getOwner().getName() + " 的宠物:";
               }

               logger.error(preStr + "(" + owner.getType() + "-" + owner.getName() + ") 攻击：" + defenerPreStr + "(" + creature.getType() + " - " + creature.getName() + ") " + " 原因是没有地图    " + des);
            } else if (owner.getMap().equals(creature.getMap()) && shape.contains(creature.getActualPosition())) {
               boolean canAttack = true;
               switch(owner.getType()) {
               case 1:
                  canAttack = creature.canBeAttackedByPlayer((Player)owner) == 1;
               case 2:
               case 3:
               default:
                  break;
               case 4:
                  canAttack = creature.canBeAttackedByPlayer(((Pet)owner).getOwner()) == 1;
               }

               if (canAttack) {
                  effectedList.add(creature);
               }
            }
         }
      }

      aggroList.clear();
      aggroList = null;
   }

   protected static void chooseRandomTargetFilterHas(List allTargets, List hasTargets, int chooseCount, Creature owner, boolean includeSelf) {
      int count = 0;
      if (!includeSelf) {
         allTargets.remove(owner);
      }

      if (allTargets.size() > 0) {
         do {
            Creature target = (Creature)allTargets.remove(Rnd.get(allTargets.size()));
            boolean has = false;
            Iterator var9 = hasTargets.iterator();

            while(var9.hasNext()) {
               Creature ht = (Creature)var9.next();
               if (ht.getType() == target.getType() && ht.getID() == target.getID()) {
                  has = true;
                  break;
               }
            }

            if (!has) {
               hasTargets.add(target);
               ++count;
            }
         } while(count < chooseCount && allTargets.size() >= 1);
      }

   }

   public static void getMonsters(Creature owner, Shape shape, Point point, List effectedList) {
      List monsters = owner.getMap().getCanAttackMonster(owner, shape, point);
      effectedList.addAll(monsters);
      monsters.clear();
      monsters = null;
   }

   public static void getAttackPlayers(Creature owner, Shape shape, Point point, List effectedList) {
      getPlayers(owner, shape, point, false, false, effectedList);
   }

   public static void getPlayers(Creature owner, Shape shape, Point point, boolean includeSelf, boolean includeDie, List effectedList) {
      List players = owner.getMap().getPlayerByRange(owner, shape, point, false, false);
      effectedList.addAll(players);
      players.clear();
      players = null;
   }

   public static void getPets(Creature owner, Shape shape, Point point, List effectedList) {
   }

   public static List getFriendlyTargets(Skill skill, Point point) {
      Creature owner = skill.getOwner();
      Shape shape = getArea(skill, point);
      List effectedList = null;
      switch(owner.getType()) {
      case 1:
      case 4:
         effectedList = RoleTargetSelectedManager.getFriendlyTargets(skill, point, shape);
         break;
      case 2:
      case 3:
         effectedList = OtherTargetSeletedManager.getFriendlyTargets(skill, point, shape);
      }

      return effectedList;
   }
}
