package com.mu.game.model.unit.skill.skills.inter;

import com.mu.game.model.unit.Creature;
import java.awt.Point;

public interface GroupGainSkill {
   int datumSelfPreCheck();

   int datumTargetPreCheck(Creature var1);

   int datumMousePreCheck(Point var1);
}
