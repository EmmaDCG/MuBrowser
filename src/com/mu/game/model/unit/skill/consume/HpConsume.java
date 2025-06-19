package com.mu.game.model.unit.skill.consume;

import com.mu.game.model.unit.skill.Skill;

public class HpConsume implements Consume {
   private int value;

   public HpConsume(int value) {
      this.value = value;
   }

   public void consumed(Skill skill) {
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }
}
