package com.mu.game.model.unit.trigger;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.trigger.action.TriggerAction;
import com.mu.game.model.unit.trigger.action.TriggerActionFactory;
import com.mu.utils.Rnd;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class TriggerManager {
   private Creature owner;
   private ConcurrentHashMap triggerActions = null;
   private ConcurrentHashMap metuxEffects = null;
   private ConcurrentHashMap inclusiveEffects = null;

   public TriggerManager(Creature owner) {
      this.owner = owner;
      this.triggerActions = Tools.newConcurrentHashMap2();
      this.inclusiveEffects = Tools.newConcurrentHashMap2();
      this.metuxEffects = Tools.newConcurrentHashMap2();
   }

   public boolean isHandle(int type) {
      return this.inclusiveEffects.containsKey(type) || this.metuxEffects.containsKey(type);
   }

   public void handle(int type, Object... args) {
      try {
         ConcurrentHashMap inMaps = (ConcurrentHashMap)this.inclusiveEffects.get(type);
         TriggerAction te;
         if (inMaps != null) {
            Iterator it = inMaps.keySet().iterator();

            while(it.hasNext()) {
               te = (TriggerAction)it.next();
               if (te.isEffect()) {
                  te.handle(false, args);
                  te.setLastTriggerTime(System.currentTimeMillis());
               }
            }
         }

         ConcurrentHashMap meMaps = (ConcurrentHashMap)this.metuxEffects.get(type);
         if (meMaps != null) {
            List eventList = new ArrayList();
            Iterator it = meMaps.keySet().iterator();

            while(it.hasNext()) {
               te = (TriggerAction)it.next();
               if (te.meedCondition()) {
                  eventList.add(te);
               }
            }

            if (eventList.size() > 0) {
               te = (TriggerAction)eventList.get(Rnd.get(eventList.size()));
               te.handle(true, args);
               te.setLastTriggerTime(System.currentTimeMillis());
            }

            eventList.clear();
            te = null;
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public void addTriggerActions(int statID, List modifies) {
      this.removeTriggerAction(statID);
      List actionList = TriggerActionFactory.getTriggerAtions(modifies);
      if (actionList != null && actionList.size() > 0) {
         this.triggerActions.put(statID, actionList);
         Iterator var5 = actionList.iterator();

         while(var5.hasNext()) {
            TriggerAction action = (TriggerAction)var5.next();
            this.addEvent(action);
         }
      }

   }

   public void removeTriggerAction(int statID) {
      List actionList = (List)this.triggerActions.remove(statID);
      if (actionList != null) {
         Iterator var4 = actionList.iterator();

         while(var4.hasNext()) {
            TriggerAction action = (TriggerAction)var4.next();
            this.removeEvent(action);
         }

         actionList.clear();
         actionList = null;
      }
   }

   private void addEvent(TriggerAction event) {
      event.setEffect(true);
      ConcurrentHashMap map = null;
      if (event.isMetux()) {
         map = (ConcurrentHashMap)this.metuxEffects.get(event.getType());
         if (map == null) {
            map = new ConcurrentHashMap();
            this.metuxEffects.put(event.getType(), map);
         }
      } else {
         map = (ConcurrentHashMap)this.inclusiveEffects.get(event.getType());
         if (map == null) {
            map = new ConcurrentHashMap();
            this.inclusiveEffects.put(event.getType(), map);
         }
      }

      event.setOwner(this.getOwner());
      map.put(event, true);
   }

   private void removeEvent(TriggerAction event) {
      event.setEffect(false);
      ConcurrentHashMap map = null;
      if (event.isMetux()) {
         map = (ConcurrentHashMap)this.metuxEffects.get(event.getType());
      } else {
         map = (ConcurrentHashMap)this.inclusiveEffects.get(event.getType());
      }

      map.remove(event);
      if (map.size() < 1) {
         if (event.isMetux()) {
            this.metuxEffects.remove(event.getType());
         } else {
            this.inclusiveEffects.remove(event.getType());
         }
      }

   }

   public void handleAttack(HashMap results) {
      if (this.isHandle(1)) {
         if (results != null && results.size() >= 1) {
            this.handle(1, results);
         }
      }
   }

   public void handleMove() {
      if (this.isHandle(6)) {
         this.handle(6);
      }
   }

   public void handleUseSkill(Skill skill) {
      if (this.isHandle(7)) {
         this.handle(7, skill);
      }
   }

   public void handleAttackHit(HashMap results) {
      if (this.isHandle(5)) {
         if (results != null && results.size() >= 1) {
            boolean hit = false;
            Iterator it = results.values().iterator();

            while(it.hasNext()) {
               AttackResult result = (AttackResult)it.next();
               if (result.getType() != 5 && result.getType() != 2) {
                  hit = true;
                  break;
               }
            }

            if (hit) {
               this.handle(5, results);
            }

         }
      }
   }

   public void handleKillMonster(Monster monster) {
      if (this.isHandle(4)) {
         if (monster != null) {
            this.handle(4, monster);
         }
      }
   }

   public void handleBeAttacked(Creature attacker, AttackResult result) {
      if (this.isHandle(2)) {
         if (attacker != null) {
            this.handle(2, attacker, result);
         }
      }
   }

   public void destroy() {
      Iterator it;
      Entry entry;
      if (this.triggerActions != null) {
         it = this.triggerActions.entrySet().iterator();

         while(it.hasNext()) {
            entry = (Entry)it.next();
            List actionList = (List)entry.getValue();
            it.remove();
            actionList.clear();
         }

         this.triggerActions.clear();
         this.triggerActions = null;
      }

      ConcurrentHashMap actionMap;
      if (this.inclusiveEffects != null) {
         it = this.inclusiveEffects.entrySet().iterator();

         while(it.hasNext()) {
            entry = (Entry)it.next();
            actionMap = (ConcurrentHashMap)entry.getValue();
            it.remove();
            actionMap.clear();
         }

         this.inclusiveEffects.clear();
         this.inclusiveEffects = null;
      }

      if (this.metuxEffects != null) {
         it = this.metuxEffects.entrySet().iterator();

         while(it.hasNext()) {
            entry = (Entry)it.next();
            actionMap = (ConcurrentHashMap)entry.getValue();
            it.remove();
            actionMap.clear();
         }

         this.metuxEffects.clear();
         this.metuxEffects = null;
      }

      this.owner = null;
   }

   public Creature getOwner() {
      return this.owner;
   }

   public ConcurrentHashMap getTriggerActions() {
      return this.triggerActions;
   }

   public ConcurrentHashMap getInclusiveEffects() {
      return this.inclusiveEffects;
   }

   public ConcurrentHashMap getMetuxEffects() {
      return this.metuxEffects;
   }
}
