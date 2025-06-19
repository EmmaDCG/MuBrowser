package com.mu.game.model.unit.trigger.action.imp;

import com.mu.game.model.item.action.imp.AddHp;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.trigger.action.AbsAttackAction;
import com.mu.utils.Rnd;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class AttackTargetReduceHpAction extends AbsAttackAction {
   int value;
   protected StatEnum triggerStat;
   protected StatEnum resStat;

   public AttackTargetReduceHpAction(int id, int value, StatEnum triggerStat, StatEnum resStat) {
      super(id);
      this.value = value;
      this.triggerStat = triggerStat;
      this.resStat = resStat;
   }

   public void attackMultiple(HashMap results) {
      Iterator it = results.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         Creature defencer = (Creature)entry.getKey();
         if (this.canTrigger(defencer, (AttackResult)entry.getValue())) {
            AddHp.reduceHp(defencer, this.getValue(), this.getOwner());
         }
      }

   }

   protected boolean canTrigger(Creature defencer, AttackResult result) {
      if (defencer != null && !defencer.isDestroy() && !defencer.isDie() && !defencer.isDamageImmunity()) {
         int tmpRate = this.getOwner().getStatValue(this.triggerStat) - defencer.getStatValue(this.resStat);
         return Rnd.get(100000) <= tmpRate;
      } else {
         return false;
      }
   }

   public boolean privyCondition() {
      return true;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }
}
