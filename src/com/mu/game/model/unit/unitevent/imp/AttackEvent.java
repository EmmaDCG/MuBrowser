package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;

public class AttackEvent extends Event {
   private Creature target;

   public AttackEvent(Creature owner, Creature target) {
      super(owner);
      this.target = target;
   }

   public void work(long now) throws Exception {
      if (!((Creature)this.getOwner()).operationInLimit(OperationEnum.ATTACK) && !((Creature)this.getOwner()).operationInLimit(OperationEnum.USESKILL)) {
         long tmp = now - ((Creature)this.getOwner()).getLastAttackTime();
         if (tmp >= (long)((Creature)this.getOwner()).getSkillManager().getAttackSpeed()) {
            if (this.target != null && !this.target.isDie() && !this.target.isDestroy() && !((Creature)this.getOwner()).isDie()) {
               HashMap skillMap = ((Creature)this.getOwner()).getSkillManager().getSkillMap();
               Point point = this.target.getActualPosition();
               Skill useSkil = null;
               int coolTime = 0;
               Iterator var10 = skillMap.values().iterator();

               while(var10.hasNext()) {
                  Skill skill = (Skill)var10.next();
                  int result = 1;
                  if (skill != null) {
                     result = skill.preCastCheck(point, this.target);
                     if (result == 1 && skill.getCoolTime() > coolTime) {
                        coolTime = skill.getCoolTime();
                        useSkil = skill;
                     }
                  }
               }

               if (useSkil != null) {
                  useSkil.useSkill(true, point, this.target);
                  ((Creature)this.getOwner()).setLastAttackTime(now);
               }

               if (this.target.isDie()) {
                  ((Creature)this.getOwner()).idle();
               }

            } else {
               ((Creature)this.getOwner()).idle();
            }
         }
      } else {
         ((Creature)this.getOwner()).idle();
      }
   }

   public Status getStatus() {
      return Status.ATTACK;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.ATTACK;
   }

   public Creature getTarget() {
      return this.target;
   }

   public void setTarget(Creature target) {
      this.target = target;
   }
}
