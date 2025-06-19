package com.mu.game.model.unit.skill.condition;

import com.mu.game.model.unit.skill.Skill;

public class StatusCondition implements Condition {
   private int value;

   public StatusCondition(int value) {
      this.value = value;
   }

   public int verify(Skill skill) {
      return 1;
   }

   public int getType() {
      return 5;
   }

   public int getValue() {
      return this.value;
   }

   public int getConsumeValue() {
      return 0;
   }
}
