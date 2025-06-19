package com.mu.game.model.unit.skill.skills.impl;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.skill.action.SkillAction;
import java.awt.Point;
import java.awt.Rectangle;

public class SprintSkill extends SingleAttackSkill {
   public SprintSkill(int skillID, int level, Creature owner) {
      super(skillID, level, owner);
   }

   public boolean isSprint() {
      return true;
   }

   protected void doSendAndAction(AttackResult result, Creature effected, Point centPoint) {
      SkillAction action = this.getModel().getAction();
      if (action != null) {
         action.singleAction(this, effected, result, centPoint);
      }

      this.singleSend(result, effected, centPoint);
      Point endPoint = effected.getActualPosition();
      com.mu.io.game.packet.imp.skill.SprintSkill.sendToClient(this.getOwner(), endPoint.x, endPoint.y);
      Rectangle newArea = this.getOwner().getMap().getArea(endPoint.x, endPoint.y);
      Rectangle oldArea = this.getOwner().getArea();
      if (newArea != null) {
         this.getOwner().setPosition(endPoint.x, endPoint.y);
         if (!newArea.equals(oldArea)) {
            this.getOwner().switchArea(newArea, oldArea);
         }
      }

   }
}
