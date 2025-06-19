package com.mu.game.model.equip.compositenew;

import com.mu.game.model.weight.WeightAtom;

public class ComTargetWeightAtom extends WeightAtom {
   private int itemModelID;

   public ComTargetWeightAtom(int weight, int itemModelID) {
      super(weight);
      this.itemModelID = itemModelID;
   }

   public int getItemModelID() {
      return this.itemModelID;
   }

   public void setItemModelID(int itemModelID) {
      this.itemModelID = itemModelID;
   }
}
