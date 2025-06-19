package com.mu.game.model.unit.skill.skills;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.skill.Skill;
import com.mu.io.game.packet.imp.skill.ReleaseSkill;
import com.mu.io.game.packet.imp.skill.UpdateSkillCD;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class GroupSkill extends Skill {
   public GroupSkill(int skillID, int level, Creature owner) {
      super(skillID, level, owner);
   }

   public void groupAttackSend(HashMap results, Point facePoint, Creature target) {
      if (results == null) {
         results = new HashMap();
      }

      ReleaseSkill.sendToClient(this, results, facePoint, target);
      UpdateSkillCD.sendToClient((Skill)this);
   }

   public HashMap createResultSet(List effectedList) {
      HashMap results = new HashMap();
      if (effectedList != null) {
         Iterator var4 = effectedList.iterator();

         while(var4.hasNext()) {
            Creature creature = (Creature)var4.next();
            AttackResult result = AttackResult.createNoneResult(creature, this.getSkillID());
            results.put(creature, result);
         }
      }

      return results;
   }
}
