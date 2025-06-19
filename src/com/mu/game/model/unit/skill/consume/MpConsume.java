package com.mu.game.model.unit.skill.consume;

import com.mu.game.model.item.action.imp.AddMp;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.skill.Skill;

public class MpConsume implements Consume {
   private int value;

   public MpConsume(int value) {
      this.value = value;
   }

   public void consumed(Skill skill) {
      int tmpValue = skill.getMpConsume(this.value);
      Creature owner = skill.getOwner();
      if (tmpValue > 0) {
         AddMp.reduceMPAndSend(owner, tmpValue);
      }

   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }
}
