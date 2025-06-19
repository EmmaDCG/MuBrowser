package com.mu.game.model.stats;

public enum StatModifyPriority {
   NONE(0),
   ADD(1),
   RATE(2),
   GLOBLERATE(3);

   private int priority;

   private StatModifyPriority(int priority) {
      this.priority = priority;
   }

   public int getPriority() {
      return this.priority;
   }

   public boolean isPercent() {
      switch(this.priority) {
      case 2:
      case 3:
         return true;
      default:
         return false;
      }
   }

   public static StatModifyPriority fine(int type) {
      StatModifyPriority[] var4;
      int var3 = (var4 = values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         StatModifyPriority priority = var4[var2];
         if (priority.getPriority() == type) {
            return priority;
         }
      }

      return NONE;
   }
}
