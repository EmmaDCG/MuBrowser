package com.mu.game.model.unit.skill.action.impl;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.action.SkillAction;
import com.mu.game.model.unit.skill.skills.GroupSkill;
import com.mu.game.model.unit.skill.skills.SingleSkill;
import com.mu.io.game.packet.imp.attack.AttackCreature;
import com.mu.io.game.packet.imp.attack.CreaturePositionCorrect;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;

public class S_TeleportAction extends SkillAction {
   public S_TeleportAction(int type) {
      super(type);
   }

   public void singleAction(SingleSkill skill, Creature effected, AttackResult result, Point centPoint) {
      Creature owner = skill.getOwner();
      int x = centPoint.x;
      int y = centPoint.y;
      CreaturePositionCorrect.correntWhenTeleport(owner, x, y);
      Rectangle newArea = owner.getMap().getArea(x, y);
      Rectangle oldArea = owner.getArea();
      owner.setPosition(x, y);
      if (newArea != null && !newArea.equals(oldArea)) {
         owner.switchArea(newArea, oldArea);
      }

   }

   public void groupAction(GroupSkill skill, HashMap results, Point centPoint) {
   }

   public int preCheck(Skill skill, Creature effected, Point specifyPoint) {
      Creature owner = skill.getOwner();
      Map map = owner.getMap();
      if (map.isBlocked(specifyPoint.x, specifyPoint.y)) {
         return 8038;
      } else {
         int result = AttackCreature.distanceCheck(effected.getActualPosition(), specifyPoint, skill.getModel().getDistance());
         if (result != 1) {
            int allDistance = MathUtil.getDistance(owner.getActualPosition(), specifyPoint);
            double rate = 1.0D * (double)skill.getModel().getDistance() / (double)allDistance;
            int x = (int)((double)owner.getX() + (double)(specifyPoint.x - owner.getX()) * rate);
            int y = (int)((double)owner.getY() + (double)(specifyPoint.y - owner.getY()) * rate);
            if (map.isBlocked(x, y)) {
               return 8039;
            }

            specifyPoint.x = x;
            specifyPoint.y = y;
         }

         return 1;
      }
   }

   public void initCheck(String des) throws Exception {
   }
}
