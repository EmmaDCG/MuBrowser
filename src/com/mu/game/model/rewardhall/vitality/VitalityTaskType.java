package com.mu.game.model.rewardhall.vitality;

public enum VitalityTaskType {
   FB_Enter_Count(1),
   KILL_MONSTER(2),
   QHZB(3),
   XB(4),
   JFDH(5),
   XFBZ(6),
   RWJS(7);

   private int id;

   private VitalityTaskType(int id) {
      this.id = id;
   }

   public int getId() {
      return this.id;
   }

   public static VitalityTaskType valueOf(int id) {
      VitalityTaskType[] var4;
      int var3 = (var4 = values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         VitalityTaskType type = var4[var2];
         if (type.getId() == id) {
            return type;
         }
      }

      return null;
   }
}
