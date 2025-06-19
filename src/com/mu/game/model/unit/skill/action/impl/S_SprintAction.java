package com.mu.game.model.unit.skill.action.impl;

import com.mu.game.model.map.AStar;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.buff.model.BuffDynamicData;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.skills.GroupSkill;
import com.mu.game.model.unit.skill.skills.SingleSkill;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;

public class S_SprintAction extends S_Buff_Action {
   public S_SprintAction(int type, int buffID, int buffLevel, boolean levelBySkill) {
      super(type, buffID, buffLevel, levelBySkill);
   }

   public int preCheck(Skill skill, Creature effected, Point specifyPoint) {
      Creature owner = skill.getOwner();
      return AStar.isLineBlocked(owner.getMap(), owner.getActualPosition(), effected.getActualPosition()) ? 8042 : 1;
   }

   public void singleAction(SingleSkill skill, Creature effected, AttackResult result, Point centPoint) {
      long length = (long)MathUtil.getDistance(skill.getOwner().getActualPosition(), effected.getActualPosition());
      BuffDynamicData data = BuffDynamicData.getDyData(this.getBuffID(), this.getBuffLevel());
      long time = (long)data.getDuration();
      long addTime = length / 2000L;
      if (addTime > time) {
         addTime -= time;
      } else {
         addTime = 0L;
      }

      skill.getOwner().getBuffManager().createAndStartBuff(skill.getOwner(), this.getBuffID(), this.getBuffLevel(), true, addTime, (List)null);
   }

   public void groupAction(GroupSkill skill, HashMap results, Point centPoint) {
   }
}
