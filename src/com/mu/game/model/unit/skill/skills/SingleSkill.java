package com.mu.game.model.unit.skill.skills;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.skill.Skill;
import com.mu.io.game.packet.imp.skill.ReleaseSkill;
import com.mu.io.game.packet.imp.skill.UpdateSkillCD;
import java.awt.Point;
import java.util.HashMap;

public abstract class SingleSkill extends Skill {
   public SingleSkill(int skillID, int level, Creature owner) {
      super(skillID, level, owner);
   }

   public void singleSend(AttackResult result, Creature effected, Point facePoint) {
      if (result == null) {
         result = new AttackResult(1, 0, this.getSkillID(), -1, this.getOwner());
      }

      HashMap results = new HashMap();
      results.put(effected, result);
      ReleaseSkill.sendToClient(this, results, facePoint, effected);
      UpdateSkillCD.sendToClient((Skill)this);
   }

   public HashMap createResult(Creature effected, AttackResult result) {
      HashMap results = new HashMap();
      results.put(effected, result);
      return results;
   }
}
