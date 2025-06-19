package com.mu.game.model.unit.skill;

import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SkillAdditionalProperty {
   private int skillID;
   private HashMap enumStats = null;
   private HashMap stats = null;

   public SkillAdditionalProperty(int skillID) {
      this.skillID = skillID;
      this.stats = new HashMap();
      this.enumStats = new HashMap();
   }

   public void addStats(int statID, List modifies) {
      this.removeStats(statID);
      this.stats.put(statID, modifies);
      this.process();
   }

   public void endStats(int statID) {
      this.removeStats(statID);
      this.process();
   }

   private void removeStats(int statID) {
      List modifies = (List)this.stats.get(statID);
      if (modifies != null) {
         modifies.clear();
      }

      modifies = null;
   }

   public void process() {
      this.enumStats.clear();
      Iterator var2 = this.stats.values().iterator();

      label30:
      while(var2.hasNext()) {
         List modifies = (List)var2.next();
         Iterator var4 = modifies.iterator();

         while(true) {
            while(true) {
               if (!var4.hasNext()) {
                  continue label30;
               }

               FinalModify modify = (FinalModify)var4.next();
               int[] values = (int[])this.enumStats.get(modify.getStat());
               if (values == null) {
                  values = new int[2];
                  this.enumStats.put(modify.getStat(), values);
               }

               if (!modify.isPercent() && !modify.getStat().isPercent()) {
                  values[0] += modify.getValue();
               } else {
                  values[1] += modify.getValue();
               }
            }
         }
      }

   }

   public int getValue(StatEnum stat, StatModifyPriority priority, int value) {
      int[] values = (int[])this.enumStats.get(stat);
      if (values != null) {
         if (priority.isPercent()) {
            value += values[1];
         } else {
            value = (int)(1.0F * (float)(100000 + values[1]) * (float)value / 100000.0F) + values[0];
         }
      }

      return value;
   }

   public void destroy() {
      if (this.stats != null) {
         this.stats.clear();
         this.stats = null;
      }

      if (this.enumStats != null) {
         this.enumStats.clear();
         this.enumStats = null;
      }

   }

   public int getSkillID() {
      return this.skillID;
   }
}
