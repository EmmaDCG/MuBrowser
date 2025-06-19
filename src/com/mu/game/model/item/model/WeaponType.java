package com.mu.game.model.item.model;

public enum WeaponType {
   None(0),
   OneHand(1),
   BothHands(2),
   SecondaryHand(3),
   MainHand(4);

   private int type;

   private WeaponType(int type) {
      this.type = type;
   }

   public static WeaponType fine(int wt) {
      switch(wt) {
      case 1:
         return OneHand;
      case 2:
         return BothHands;
      case 3:
         return SecondaryHand;
      case 4:
         return MainHand;
      default:
         return None;
      }
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }
}
