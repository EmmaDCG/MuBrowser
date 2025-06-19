package com.mu.game.model.unit.buff.requirement.impl;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.requirement.EffectRequirement;

public class BaiduMemberRequirement extends EffectRequirement {
   private int memberLevel;

   public BaiduMemberRequirement(int type, int memberLevel) {
      super(type);
      this.memberLevel = memberLevel;
   }

   public boolean check(Creature creature) {
      return false;
   }

   public int getMemberLevel() {
      return this.memberLevel;
   }

   public void setMemberLevel(int memberLevel) {
      this.memberLevel = memberLevel;
   }
}
