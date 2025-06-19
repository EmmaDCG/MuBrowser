package com.mu.game.model.unit.buff.requirement.impl;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.requirement.EffectRequirement;
import com.mu.game.model.unit.player.Player;

public class MicroClientRequirement extends EffectRequirement {
   public MicroClientRequirement(int type) {
      super(type);
   }

   public boolean check(Creature creature) {
      return creature.getType() == 1 && ((Player)creature).isClient();
   }
}
