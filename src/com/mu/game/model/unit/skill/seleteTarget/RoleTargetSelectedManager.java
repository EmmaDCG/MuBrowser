package com.mu.game.model.unit.skill.seleteTarget;

import com.mu.game.model.pet.Pet;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.pkMode.PkEnum;
import com.mu.game.model.unit.service.EvilManager;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.skills.AbsSingleGainSkill;
import com.mu.io.game.packet.imp.attack.AttackCreature;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoleTargetSelectedManager {
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum;

   public static List getAttackTargets(Skill skill, Point point, Shape shape) {
      Player player = null;
      switch(skill.getOwner().getType()) {
      case 1:
         player = (Player)skill.getOwner();
      case 2:
      case 3:
      default:
         break;
      case 4:
         player = ((Pet)skill.getOwner()).getOwner();
      }

      List effectedList = new ArrayList();
      if (player != null) {
         SkillTargetSeleteManager.getMonsters(player, shape, point, effectedList);
         getAttackPlayers(skill, point, shape, player, effectedList);
      }

      return effectedList;
   }

   private static void getAttackPlayers(Skill skill, Point point, Shape shape, Player player, List effectedList) {
      if (EvilManager.canFightByEvil(player) == 1) {
         List players = player.getMap().getPlayerByRange(player, shape, point, false, false);
         boolean canPk = false;
         Iterator var8 = players.iterator();

         while(var8.hasNext()) {
            Player p = (Player)var8.next();
            canPk = AttackCreature.getPkCheckResult(player, p) == 1;
            if (canPk) {
               effectedList.add(p);
            }
         }

         players.clear();
         players = null;
      }
   }

   public static List getFriendlyTargets(Skill skill, Point point, Shape shape) {
      List effectedList = new ArrayList();
      Player player = null;
      boolean addPet = skill.hasInEffectType(4);
      switch(skill.getOwner().getType()) {
      case 1:
         player = (Player)skill.getOwner();
      case 2:
      case 3:
      default:
         break;
      case 4:
         player = ((Pet)skill.getOwner()).getOwner();
      }

      if (player != null) {
         switch(skill.getModel().getEffectedObject()) {
         case 1:
            effectedList.add(skill.getOwner());
            break;
         case 2:
         case 3:
         default:
            System.err.println("技能群体类型配置出错 + " + skill.getSkillID());
            getFriendlyByPkMode(player, skill.getOwner(), skill, point, shape, true, addPet, effectedList);
            break;
         case 4:
            getFriendlyByPkMode(player, skill.getOwner(), skill, point, shape, true, addPet, effectedList);
            break;
         case 5:
            getFriendlyByPkMode(player, skill.getOwner(), skill, point, shape, false, addPet, effectedList);
            break;
         case 6:
            if (skill.getOwner().getType() == 4) {
               effectedList.add(player);
            }
            break;
         case 7:
            effectedList.add(player);
            if (skill.getOwner().getType() == 4) {
               effectedList.add(skill.getOwner());
            }
         }
      }

      return effectedList;
   }

   private static void getFriendlyByPkMode(Player player, Creature owner, Skill skill, Point point, Shape shape, boolean includeSelf, boolean addPet, List effectedList) {
      if (includeSelf) {
         effectedList.add(owner);
      }

      switch($SWITCH_TABLE$com$mu$game$model$unit$player$pkMode$PkEnum()[player.getPkMode().getCurrentPKMode().ordinal()]) {
      case 3:
         if (owner.getType() == 4) {
            effectedList.add(player);
         } else if (addPet) {
            addPet(player, shape, effectedList);
         }
         break;
      default:
         List plist = owner.getMap().getPlayerByRange(owner, shape, owner.getPosition(), true, false);
         Iterator var10 = plist.iterator();

         while(var10.hasNext()) {
            Player p = (Player)var10.next();
            if (p.getID() == player.getID()) {
               if (owner.getType() == 4) {
                  effectedList.add(player);
               } else if (addPet) {
                  addPet(player, shape, effectedList);
               }
            } else {
               boolean isInsameParty = AbsSingleGainSkill.inRoleSameParty(player, p);
               if (isInsameParty) {
                  effectedList.add(p);
                  if (addPet) {
                     addPet(p, shape, effectedList);
                  }
               }
            }
         }
      }

   }

   private static void addPet(Player player, Shape shape, List effectedList) {
      Pet pet = player.getPetManager().getActivePet();
      if (pet != null && shape.contains(pet.getActualPosition())) {
         effectedList.add(pet);
      }

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
