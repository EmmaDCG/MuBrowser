package com.mu.game.model.unit.skill.skills.inter;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;

public interface SingleAttackSkill {
   int singleEffectedCheck(Creature var1);

   void checkEvilenum(Creature var1);

   void effectedBeingAttacked(Creature var1, AttackResult var2);

   void attackTrigger(Creature var1, AttackResult var2);
}
