package com.mu.game.model.vip.effect;

public enum VIPEffectType {
   VE_1(1, 1),
   VE_2(2, 2),
   VE_3(3, 2),
   VE_4(4, 2),
   VE_6(6, 1),
   VE_7(7, 2),
   VE_8(8, 2),
   VE_9(9, 2),
   VE_10(10, 2),
   VE_11(11, 2),
   VE_12(12, 1),
   VE_13(13, 1),
   VE_14(14, 1),
   VE_15(15, 1),
   VE_16(16, 2),
   VE_17(17, 2),
   VE_18(18, 2),
   VE_19(19, 1),
   VE_20(20, 1),
   VE_21(21, 1),
   VE_22(22, 2),
   VE_23(23, 2),
   VE_24(24, 2),
   VE_25(25, 2);

   private int id;
   private int value;

   private VIPEffectType(int id, int value) {
      this.id = id;
      this.value = value;
   }

   public int getId() {
      return this.id;
   }

   public static VIPEffectType valueOf(int id) {
      for(int i = 0; i < values().length; ++i) {
         if (values()[i].getId() == id) {
            return values()[i];
         }
      }

      return null;
   }

   public int getValue() {
      return this.value;
   }
}
