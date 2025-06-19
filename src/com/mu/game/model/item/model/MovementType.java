package com.mu.game.model.item.model;

public enum MovementType {
   None(0),
   OneHand(1),
   BothHands_H(2),
   BOW(3),
   CrossBow(4),
   BothHands_L(5),
   Staff(6),
   Staff_L(7);

   private int type;

   private MovementType(int type) {
      this.type = type;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }
}
