package com.mu.game.model.task.clazz;

public enum TaskClazz {
   ZJ(1),
   RC(2),
   XS(4),
   GH(5),
   ZZ(6),
   ZX(7),
   TanXian(8);

   private int value;

   private TaskClazz(int value) {
      this.value = value;
   }

   public static TaskClazz valueOf(int value) {
      for(int i = 0; i < values().length; ++i) {
         if (values()[i].value == value) {
            return values()[i];
         }
      }

      return null;
   }

   public int getValue() {
      return this.value;
   }
}
