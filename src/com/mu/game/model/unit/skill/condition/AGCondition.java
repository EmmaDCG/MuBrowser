package com.mu.game.model.unit.skill.condition;

import com.mu.game.model.unit.skill.Skill;

public class AGCondition implements Condition {
   private int value;

   public AGCondition(int value) {
      this.value = value;
   }

   public int verify(Skill skill) {
      int tmpValue = skill.getAgConsume(this.value);
      return skill.getOwner().getAg() < tmpValue ? 8003 : 1;
   }

   public int getType() {
      return 3;
   }

   public int getConsumeValue() {
      return this.value;
   }
}
