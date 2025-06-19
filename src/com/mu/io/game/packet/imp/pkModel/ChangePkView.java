package com.mu.io.game.packet.imp.pkModel;

import com.mu.game.CenterManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.team.TeamManager;
import com.mu.game.model.team.Teammate;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.pkMode.PkEnum;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.attack.AttackCreature;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChangePkView extends WriteOnlyPacket {
   public ChangePkView(Creature creature, boolean canPk) {
      super(32011);

      try {
         this.writeShort(1);
         this.setData(creature, canPk);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public ChangePkView(Player owner, List creatures) {
      super(32011);

      try {
         this.writeShort(creatures.size());
         Iterator var4 = creatures.iterator();

         while(var4.hasNext()) {
            Creature creature = (Creature)var4.next();
            this.setData(creature, canSeeKillView(owner, creature));
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void setData(Creature creature, boolean canPk) throws Exception {
      this.writeByte(creature.getType());
      this.writeDouble((double)creature.getID());
      this.writeBoolean(canPk);
   }

   public static boolean canSeeKillView(Player owner, Creature creature) {
      switch(creature.getType()) {
      case 1:
         if (AttackCreature.getPkViewByMode(owner, (Player)creature) == 1) {
            return true;
         }

         return false;
      case 2:
      default:
         return true;
      case 3:
         return false;
      case 4:
         return AttackCreature.getPkViewByMode(owner, ((Pet)creature).getOwner()) == 1;
      }
   }

   public static void sendWhenChangePkMode(Player player, PkEnum lastEnum) {
      List players = player.getMap().getAroundPlayers(player.getPosition());
      boolean change = player.getPkMode().getCurrentPKMode() == PkEnum.Mode_Massacre || lastEnum == PkEnum.Mode_Massacre;
      List allCreatures = new ArrayList();
      ChangePkView otherPkView;
      if (players.size() > 0) {
         otherPkView = new ChangePkView(player, true);
         ChangePkView otherNotPkView = new ChangePkView(player, false);
         ChangePkView petPkView = null;
         ChangePkView petNotPkView = null;
         Pet pet = player.getPetManager().getActivePet();
         if (pet != null) {
            petPkView = new ChangePkView(pet, true);
            petNotPkView = new ChangePkView(pet, false);
         }

         Iterator var11 = players.iterator();

         while(var11.hasNext()) {
            Player other = (Player)var11.next();
            if (other.getID() != player.getID()) {
               boolean canSee = canSeeKillView(other, player);
               if (canSee) {
                  other.writePacket(otherPkView);
                  if (petPkView != null) {
                     other.writePacket(petPkView);
                  }
               } else {
                  other.writePacket(otherNotPkView);
                  if (petNotPkView != null) {
                     other.writePacket(petNotPkView);
                  }
               }

               if (change) {
                  allCreatures.add(other);
                  if (pet != null) {
                     allCreatures.add(pet);
                  }
               }
            }
         }

         otherPkView.destroy();
         otherPkView = null;
         otherNotPkView.destroy();
         otherNotPkView = null;
      }

      if (allCreatures.size() > 0) {
         otherPkView = new ChangePkView(player, allCreatures);
         player.writePacket(otherPkView);
         otherPkView.destroy();
         otherPkView = null;
      }

      players.clear();
      players = null;
      allCreatures.clear();
      allCreatures = null;
   }

   public static void sendWhenAddToTeam(Player player) {
      List list = player.getCurrentTeam().getMateList();
      if (list.size() > 1) {
         List others = new ArrayList();
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            Teammate tm = (Teammate)var4.next();
            if (tm.getId() != player.getID()) {
               Player other = CenterManager.getPlayerByRoleID(tm.getId());
               if (other != null && !other.isDestroy() && other.getMap().equals(player.getMap())) {
                  Pet pet;
                  if (other.getPkMode().getCurrentPKMode() != PkEnum.Mode_Massacre) {
                     changePkViewForSomeone(other, player, false);
                     pet = player.getPetManager().getActivePet();
                     if (pet != null) {
                        changePkViewForSomeone(other, pet, false);
                     }
                  }

                  if (player.getPkMode().getCurrentPKMode() != PkEnum.Mode_Massacre) {
                     others.add(other);
                     pet = player.getPetManager().getActivePet();
                     if (pet != null) {
                        others.add(pet);
                     }
                  }
               }
            }
         }

         if (others.size() > 0) {
            changePkViewForSomeone(player, others);
         }

         others.clear();
         others = null;
      }

   }

   public static void sendWhenRemoveFromTeam(Player player, List oldTeamers) {
      if (oldTeamers.size() > 1) {
         List others = new ArrayList();
         Iterator var4 = oldTeamers.iterator();

         while(var4.hasNext()) {
            Teammate tm = (Teammate)var4.next();
            Player other = CenterManager.getPlayerByRoleID(tm.getId());
            if (other != null && !other.isDestroy() && other.getMap().equals(player.getMap())) {
               if (other.getPkMode().getCurrentPKMode() != PkEnum.Mode_Massacre) {
                  boolean cskv = canSeeKillView(other, player);
                  changePkViewForSomeone(other, player, cskv);
                  Pet pet = player.getPetManager().getActivePet();
                  if (pet != null) {
                     changePkViewForSomeone(other, pet, cskv);
                  }
               }

               if (player.getPkMode().getCurrentPKMode() != PkEnum.Mode_Massacre) {
                  others.add(other);
                  Pet pet = player.getPetManager().getActivePet();
                  if (pet != null) {
                     others.add(pet);
                  }
               }
            }
         }

         if (others.size() > 0) {
            changePkViewForSomeone(player, others);
         }

         others.clear();
         others = null;
      }

   }

   public static void sendWhenAddOrQuit4Gang(Player player, Gang gang) {
      if (gang != null) {
         List others = new ArrayList();
         List arounds = player.getMap().getAroundPlayers(player.getPosition());
         Iterator var5 = arounds.iterator();

         while(var5.hasNext()) {
            Player p = (Player)var5.next();
            if (p.getID() != player.getID() && !TeamManager.isTeammate(p, player)) {
               if (p.getPkMode().getCurrentPKMode() != PkEnum.Mode_Massacre) {
                  boolean cskv = canSeeKillView(p, player);
                  changePkViewForSomeone(p, player, cskv);
                  Pet pet = player.getPetManager().getActivePet();
                  if (pet != null) {
                     changePkViewForSomeone(p, pet, cskv);
                  }
               }

               if (player.getPkMode().getCurrentPKMode() != PkEnum.Mode_Massacre) {
                  others.add(p);
                  Pet pet = player.getPetManager().getActivePet();
                  if (pet != null) {
                     others.add(pet);
                  }
               }
            }
         }

         if (others.size() > 0) {
            changePkViewForSomeone(player, others);
         }

         others.clear();
         others = null;
         arounds.clear();
         arounds = null;
      }
   }

   private static void changePkViewForSomeone(Player player, Creature creature, boolean canSee) {
      ChangePkView cpv = new ChangePkView(creature, canSee);
      player.writePacket(cpv);
      cpv.destroy();
      cpv = null;
   }

   private static void changePkViewForSomeone(Player player, List creatures) {
      ChangePkView cpv = new ChangePkView(player, creatures);
      player.writePacket(cpv);
      cpv.destroy();
      cpv = null;
   }
}
