package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.action.SkillAction;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import java.util.Iterator;
import java.util.List;

public class AutoAssSkillEvent extends Event {
   public AutoAssSkillEvent(Player owner) {
      super(owner);
      this.lastCheckTime = System.currentTimeMillis();
   }

   public void work(long now) throws Exception {
      if (!((Player)this.getOwner()).operationInLimit(this.getOperationEnum())) {
         if (!((Player)this.getOwner()).isMoving()) {
            if (!((Player)this.getOwner()).getSkillManager().isInCommonCoolTime(now)) {
               if (((Player)this.getOwner()).isInHanging()) {
                  List assSkills = ((Player)this.getOwner()).getGameHang().getCurrentAssistSkills();
                  boolean assistedMode = ((Player)this.getOwner()).getGameHang().isAssistedMode();
                  Iterator var6 = assSkills.iterator();

                  while(var6.hasNext()) {
                     Integer skillID = (Integer)var6.next();
                     if (!((Player)this.getOwner()).getSkillManager().isSkillDisabled(skillID.intValue(), now)) {
                        Skill skill = ((Player)this.getOwner()).getSkillManager().getSkill(skillID.intValue());
                        if (skill.getNextThawTime() <= now) {
                           SkillAction action = skill.getModel().getAction();
                           if (action != null) {
                              Creature target = action.useTargetInHang((Player)this.getOwner(), assistedMode);
                              if (target != null) {
                                 skill.useSkill(false, target.getPosition(), target);
                              }
                           }
                        }
                     }
                  }
               }

            }
         }
      }
   }

   public Status getStatus() {
      return Status.AutoAssSkill;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.USESKILL;
   }
}
