package com.mu.game.model.unit.skill.skills.inter;

import com.mu.game.model.unit.Creature;
import java.awt.Point;
import java.util.HashMap;

public interface GroupAttackSkill {
   int datumSelfPreCheck(Creature var1);

   int datumTargetPreCheck(Creature var1);

   int datumMousePreCheck(Point var1, Creature var2);

   void checkEvilEnum();

   void effectedBeingAttacked(HashMap var1);

   void attackTrigger(HashMap var1);
}
