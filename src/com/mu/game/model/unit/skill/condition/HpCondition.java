package com.mu.game.model.unit.skill.condition;

import com.mu.game.model.unit.skill.Skill;

public class HpCondition implements Condition {
   private int value;

   public HpCondition(int value) {
      this.value = value;
   }

   public int verify(Skill skill) {
      return skill.getOwner().getHp() < this.value ? 8001 : 1;
   }

   public int getType() {
      return 1;
   }

   public int getConsumeValue() {
      return this.value;
   }
}
