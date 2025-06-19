package com.mu.game.model.unit.buff.requirement.impl;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.requirement.EffectRequirement;
import com.mu.game.model.unit.player.Player;

public class VipLevelRequirement extends EffectRequirement {
   private int vipLevel;

   public VipLevelRequirement(int type, int vipLevel) {
      super(type);
      this.vipLevel = vipLevel;
   }

   public boolean check(Creature creature) {
      return creature.getType() == 1 && ((Player)creature).getVIPLevel() >= this.getVipLevel();
   }

   public int getVipLevel() {
      return this.vipLevel;
   }
}
