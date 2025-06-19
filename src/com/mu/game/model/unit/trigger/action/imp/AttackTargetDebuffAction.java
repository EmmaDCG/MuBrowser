package com.mu.game.model.unit.trigger.action.imp;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.trigger.action.AbsAttackAction;
import com.mu.utils.Rnd;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class AttackTargetDebuffAction extends AbsAttackAction {
   protected int buffID;
   protected int level;
   protected StatEnum triggerStat;
   protected StatEnum resStat;

   public AttackTargetDebuffAction(int id, int buffID, int level, StatEnum triggerStat, StatEnum resStat) {
      super(id);
      this.buffID = buffID;
      this.level = level;
      this.triggerStat = triggerStat;
      this.resStat = resStat;
   }

   public void attackMultiple(HashMap results) {
      Iterator it = results.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         Creature defencer = (Creature)entry.getKey();
         if (this.canTrigger(defencer, (AttackResult)entry.getValue())) {
            defencer.getBuffManager().createAndStartBuff(this.getOwner(), this.getBuffID(), this.level, true, 0L, (List)null);
         }
      }

   }

   protected boolean canTrigger(Creature defencer, AttackResult result) {
      if (defencer != null && !defencer.isDestroy() && !defencer.isDie()) {
         int tmpRate = this.getOwner().getStatValue(this.triggerStat) - defencer.getStatValue(this.resStat);
         return Rnd.get(100000) <= tmpRate;
      } else {
         return false;
      }
   }

   public boolean privyCondition() {
      return true;
   }

   public int getBuffID() {
      return this.buffID;
   }

   public void setBuffID(int buffID) {
      this.buffID = buffID;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public StatEnum getTriggerStat() {
      return this.triggerStat;
   }

   public void setTriggerStat(StatEnum triggerStat) {
      this.triggerStat = triggerStat;
   }

   public StatEnum getResStat() {
      return this.resStat;
   }

   public void setResStat(StatEnum resStat) {
      this.resStat = resStat;
   }
}
