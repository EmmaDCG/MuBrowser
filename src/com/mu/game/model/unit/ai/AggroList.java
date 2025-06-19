package com.mu.game.model.unit.ai;

import com.mu.game.model.team.Team;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AggroList {
   private static Logger logger = LoggerFactory.getLogger(AggroList.class);
   public Creature owner;
   private ConcurrentHashMap aggroList = Tools.newConcurrentHashMap2();

   public AggroList(Creature owner) {
      this.owner = owner;
   }

   public void addHateByHurt(Creature creature, int hurt) {
      Creature ac = null;
      AggroInfo info = this.getAggroInfoOrAdd(creature);
      if (info != null) {
         info.addAggro(hurt, hurt);
      }

   }

   public void addHateBySkill(Creature creature, int skillHate) {
      Creature ac = null;
      AggroInfo info = this.getAggroInfoOrAdd(creature);
      if (info != null) {
         info.addAggro(skillHate, skillHate);
      }

   }

   public void addHateBySeeEnemy(Creature creature) {
      if (creature != null && !this.isHate(creature)) {
         AggroInfo info = this.getAggroInfoOrAdd(creature);
         info.addAggro(1, 0);
      }

   }

   public Creature getMostHated() {
      if (this.aggroList.isEmpty()) {
         return null;
      } else {
         Creature mostHated = null;
         int maxHate = 0;
         Iterator it = this.aggroList.keySet().iterator();

         while(it.hasNext()) {
            Creature creature = (Creature)it.next();
            AggroInfo info = (AggroInfo)this.aggroList.get(creature);
            if (info == null) {
               logger.debug("aggroList .... 对象为空 ");
            } else if (info.isInvalid()) {
               this.remove(creature);
               info.destroy();
            } else if (info.getHate() >= maxHate) {
               mostHated = creature;
               maxHate = info.getHate();
            }
         }

         return mostHated;
      }
   }

   public ConcurrentHashMap getAggroList() {
      return this.aggroList;
   }

   public ArrayList getAggroCreatureList() {
      ArrayList alist = new ArrayList();
      alist.addAll(this.aggroList.keySet());
      return alist;
   }

   public boolean isMostHated(Creature creature) {
      Creature mostHated = this.getMostHated();
      return mostHated == null ? false : mostHated.equals(creature);
   }

   private void removeAggroInfo(Creature creature) {
      AggroInfo info = (AggroInfo)this.getAggroList().remove(creature);
      if (info != null) {
         info.destroy();
      }

   }

   public void remove(Creature creature) {
      if (creature != null) {
         this.removeAggroInfo(creature);
         AggroList list = creature.getAggroList();
         if (list != null) {
            list.removeAggroInfo(this.owner);
         }

      }
   }

   public void clear() {
      Iterator it = this.aggroList.keySet().iterator();

      while(it.hasNext()) {
         this.remove((Creature)it.next());
      }

      this.aggroList.clear();
   }

   private AggroInfo getAggroInfoOrAdd(Creature target) {
      if (target == null) {
         return null;
      } else {
         AggroInfo info = (AggroInfo)this.aggroList.get(target);
         if (info == null) {
            this.aggroList.putIfAbsent(target, new AggroInfo(this.owner, target, 0, 0));
            info = (AggroInfo)this.aggroList.get(target);
         }

         return info;
      }
   }

   public boolean isHate(Creature creature) {
      return this.aggroList.containsKey(creature);
   }

   public ArrayList getHurtList() {
      ArrayList list = new ArrayList();
      HashMap teamMap = new HashMap();
      Iterator it = this.aggroList.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         Creature cr = (Creature)entry.getKey();
         AggroInfo ai = (AggroInfo)entry.getValue();
         if (cr.getType() == 1) {
            Player player = (Player)cr;
            Team team = player.getCurrentTeam();
            HurtStatisticsInfo hi;
            if (team != null) {
               hi = (HurtStatisticsInfo)teamMap.get(team.getId());
               if (hi == null) {
                  hi = new HurtStatisticsInfo();
                  hi.setTeam(true);
                  hi.setTeamId(team.getId());
                  hi.addHurt((long)ai.getHurt());
                  hi.setOwner(player);
               } else {
                  hi.addHurt((long)ai.getHurt());
                  if (team.isLeader(player.getID())) {
                     hi.setOwner(player);
                  }
               }

               list.add(hi);
            } else {
               hi = new HurtStatisticsInfo();
               hi.setTeam(true);
               hi.addHurt((long)ai.getHurt());
               hi.setOwner(player);
               list.add(hi);
            }
         }
      }

      Collections.sort(list);
      teamMap.clear();
      teamMap = null;
      return list;
   }

   public Player getWinner() {
      ArrayList list = this.getHurtList();

      try {
         if (list.size() > 0) {
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
               HurtStatisticsInfo info = (HurtStatisticsInfo)var3.next();
               Player player = info.getOwner();
               if (player != null) {
                  Player var6 = player;
                  return var6;
               }
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         list.clear();
         list = null;
      }

      return null;
   }
}
