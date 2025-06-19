package com.mu.game.model.stats;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StatModifies {
   HashMap priorityStats = new HashMap();

   public void addModify(FinalModify modify) {
      if (this.priorityStats != null) {
         StatModifyPriority priority = this.targetPriority(modify);
         if (!this.priorityStats.containsKey(priority)) {
            this.priorityStats.put(priority, new CopyOnWriteArrayList());
         }

         ((List)this.priorityStats.get(priority)).add(modify);
      }
   }

   private StatModifyPriority targetPriority(FinalModify modify) {
      StatModifyPriority priority = modify.getPriority();
      return modify.getStat().isPercent() ? StatModifyPriority.ADD : priority;
   }

   public List getStatModifies(StatModifyPriority priority) {
      return !this.priorityStats.containsKey(priority) ? null : (List)this.priorityStats.get(priority);
   }

   public boolean contains(StatModifyPriority priority) {
      return this.priorityStats == null ? false : this.priorityStats.containsKey(priority);
   }

   public int getStatSize() {
      return this.priorityStats == null ? 0 : this.priorityStats.size();
   }

   public void destroy() {
      if (this.priorityStats != null) {
         Iterator it = this.priorityStats.values().iterator();

         while(it.hasNext()) {
            ((List)it.next()).clear();
         }

         this.priorityStats.clear();
      }

   }
}
