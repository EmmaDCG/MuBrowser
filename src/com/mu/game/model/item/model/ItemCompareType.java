package com.mu.game.model.item.model;

public enum ItemCompareType {
   WEAPON(0),
   SHIELD(1),
   CLOTHES(2),
   LEGGINGS(3),
   HEAD(4),
   PHYLACTERY(5),
   NECK(6),
   WRIST(7),
   RING(8),
   FOOT(9),
   Guardian(10),
   OTHER(100);

   private int type;

   private ItemCompareType(int type) {
      this.type = type;
   }

   public static ItemCompareType find(int type) {
      ItemCompareType[] var4;
      int var3 = (var4 = values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         ItemCompareType ct = var4[var2];
         if (ct.getType() == type) {
            return ct;
         }
      }

      return OTHER;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }
}
