package com.mu.game.model.unit.skill.seleteTarget;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.skill.Skill;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OtherTargetSeletedManager {
   public static List getAttackTargets(Skill skill, Point point, Shape shape) {
      List effectedList = new ArrayList();
      Creature owner = skill.getOwner();
      switch(owner.getType()) {
      case 2:
      case 3:
         switch(owner.getType()) {
         case 2:
            SkillTargetSeleteManager.getAttackPlayers(owner, shape, point, effectedList);
            SkillTargetSeleteManager.getPets(owner, shape, point, effectedList);
            break;
         case 3:
            if (skill.hasInEffectType(0)) {
               SkillTargetSeleteManager.getAttackPlayers(owner, shape, point, effectedList);
               SkillTargetSeleteManager.getPets(owner, shape, point, effectedList);
               SkillTargetSeleteManager.getMonsters(owner, shape, point, effectedList);
            } else {
               Iterator var6 = skill.getModel().getEffectedType().iterator();

               while(var6.hasNext()) {
                  Integer creatureType = (Integer)var6.next();
                  switch(creatureType.intValue()) {
                  case 0:
                  case 3:
                  default:
                     break;
                  case 1:
                     SkillTargetSeleteManager.getAttackPlayers(owner, shape, point, effectedList);
                     break;
                  case 2:
                     SkillTargetSeleteManager.getMonsters(owner, shape, point, effectedList);
                     break;
                  case 4:
                     SkillTargetSeleteManager.getPets(owner, shape, point, effectedList);
                  }
               }
            }
         }

         return effectedList;
      default:
         return effectedList;
      }
   }

   public static List getFriendlyTargets(Skill skill, Point point, Shape shape) {
      List effectedList = new ArrayList();
      Creature owner = skill.getOwner();
      switch(owner.getType()) {
      case 2:
      case 3:
         Integer creatureType;
         Iterator var6;
         switch(owner.getType()) {
         case 2:
            if (skill.hasInEffectType(2)) {
               SkillTargetSeleteManager.getMonsters(owner, shape, point, effectedList);
               break;
            } else {
               var6 = skill.getModel().getEffectedType().iterator();

               while(var6.hasNext()) {
                  creatureType = (Integer)var6.next();
                  switch(creatureType.intValue()) {
                  case 2:
                     SkillTargetSeleteManager.getMonsters(owner, shape, point, effectedList);
                  }
               }

               return effectedList;
            }
         case 3:
            if (skill.hasInEffectType(0)) {
               SkillTargetSeleteManager.getAttackPlayers(owner, shape, point, effectedList);
               SkillTargetSeleteManager.getPets(owner, shape, point, effectedList);
            } else {
               var6 = skill.getModel().getEffectedType().iterator();

               while(var6.hasNext()) {
                  creatureType = (Integer)var6.next();
                  switch(creatureType.intValue()) {
                  case 1:
                     SkillTargetSeleteManager.getAttackPlayers(owner, shape, point, effectedList);
                  case 2:
                  case 3:
                  default:
                     break;
                  case 4:
                     SkillTargetSeleteManager.getPets(owner, shape, point, effectedList);
                  }
               }
            }
         }

         return effectedList;
      default:
         return effectedList;
      }
   }
}
