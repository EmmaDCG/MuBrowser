package com.mu.game.model.equip.weight;

import com.mu.game.model.weight.WeightAtom;

public class CreationAtom extends WeightAtom {
   private int value;

   public CreationAtom(int value, int weight) {
      super(weight);
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }
}
