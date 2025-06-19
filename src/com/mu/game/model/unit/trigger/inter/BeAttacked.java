package com.mu.game.model.unit.trigger.inter;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;

public interface BeAttacked {
   void beAttacked(Creature var1, AttackResult var2);
}
