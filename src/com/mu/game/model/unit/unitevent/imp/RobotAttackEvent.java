package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.robot.Robot;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.utils.Rnd;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RobotAttackEvent extends Event {
   private Creature target = null;

   public RobotAttackEvent(Robot owner, Creature target) {
      super(owner);
      this.target = target;
   }

   public void work(long now) throws Exception {
      if (!((Robot)this.getOwner()).operationInLimit(OperationEnum.ATTACK) && !((Robot)this.getOwner()).operationInLimit(OperationEnum.USESKILL)) {
         long tmp = now - ((Robot)this.getOwner()).getLastAttackTime();
         if (tmp >= (long)((Robot)this.getOwner()).getSkillManager().getAttackSpeed()) {
            if (this.target != null && !this.target.isDie() && !this.target.isDestroy() && !((Robot)this.getOwner()).isDie()) {
               HashMap skillMap = ((Robot)this.getOwner()).getSkillManager().getSkillMap();
               Point point = this.target.getActualPosition();
               Skill useSkil = null;
               ArrayList skillList = new ArrayList();
               Iterator var10 = skillMap.values().iterator();

               while(var10.hasNext()) {
                  Skill skill = (Skill)var10.next();
                  int result = 1;
                  if (skill != null) {
                     result = skill.preCastCheck(point, this.target);
                     if (result == 1) {
                        skillList.add(skill);
                     }
                  }
               }

               if (skillList.size() > 0) {
                  useSkil = (Skill)skillList.get(Rnd.get(skillList.size()));
                  skillList.clear();
               } else {
                  ((Robot)this.getOwner()).idle();
               }

               if (useSkil != null) {
                  useSkil.useSkill(true, point, this.target);
                  ((Robot)this.getOwner()).setLastAttackTime(now);
               }

               if (this.target.isDie()) {
                  ((Robot)this.getOwner()).idle();
               }

               skillList = null;
            } else {
               ((Robot)this.getOwner()).idle();
            }
         }
      } else {
         ((Robot)this.getOwner()).idle();
      }
   }

   public Status getStatus() {
      return Status.ATTACK;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
