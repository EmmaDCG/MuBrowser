package com.mu.game.model.unit.buff.imp;

import com.mu.game.model.unit.Creature;
import com.mu.io.game.packet.imp.skill.SprintEnd;

public class BuffSprint extends BuffCommon {
   public BuffSprint(int modelID, int level, Creature owner, Creature caster) {
      super(modelID, level, owner, caster);
   }

   public void endSpecial(boolean notice) {
      SprintEnd.sendToClient(this.getOwner());
      super.endSpecial(notice);
   }
}
