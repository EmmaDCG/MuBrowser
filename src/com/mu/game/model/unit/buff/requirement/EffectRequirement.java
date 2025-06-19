package com.mu.game.model.unit.buff.requirement;

import com.mu.game.model.unit.Creature;

public abstract class EffectRequirement {
   private int type;

   public EffectRequirement(int type) {
      this.type = type;
   }

   public abstract boolean check(Creature var1);

   public void setType(int type) {
      this.type = type;
   }

   public int getType() {
      return this.type;
   }
}
