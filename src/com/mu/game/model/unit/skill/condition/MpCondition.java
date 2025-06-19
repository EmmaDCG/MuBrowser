package com.mu.game.model.unit.skill.condition;

import com.mu.game.model.unit.skill.Skill;

public class MpCondition implements Condition {
   private int value;

   public MpCondition(int value) {
      this.value = value;
   }

   public int verify(Skill skill) {
      int tmpValue = skill.getMpConsume(this.value);
      return skill.getOwner().getMp() < tmpValue ? 8002 : 1;
   }

   public int getType() {
      return 2;
   }

   public int getConsumeValue() {
      return this.value;
   }
}
