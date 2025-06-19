package com.mu.game.model.unit.skill.condition;

import com.mu.game.model.unit.skill.Skill;

public class SiegeCondition implements Condition {
   public int verify(Skill skill) {
      return 1;
   }

   public int getType() {
      return 7;
   }

   public int getConsumeValue() {
      return 0;
   }
}
