package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.io.game.packet.imp.skill.UseSkill;
import com.mu.io.game.packet.imp.sys.BottomMessage;
import java.awt.Point;

public class UseSkillEvent extends Event {
   Skill skill;
   Point cp;
   Creature target;
   boolean clientRequest = false;

   public UseSkillEvent(Skill skill, Point cp, Creature target, boolean clientRequset) {
      super(skill.getOwner());
      this.skill = skill;
      this.cp = cp;
      this.target = target;
      this.clientRequest = clientRequset;
   }

   public static boolean canDo(Player player, Skill skill, long now) {
      if (skill.getLevel() < 1) {
         return false;
      } else if (player.operationInLimit(OperationEnum.USESKILL)) {
         return false;
      } else {
         return !skill.isDeBenefiesSkill() || System.currentTimeMillis() - player.getLastAttackTime() >= (long)player.getSkillManager().getAttackSpeed();
      }
   }

   public void work(long now) throws Exception {
      if (((Creature)this.getOwner()).operationInLimit(this.getOperationEnum())) {
         ((Creature)this.getOwner()).idle();
      } else {
         int result = 1;
         if (this.skill.getLevel() > 0) {
            if (!this.skill.isDeBenefiesSkill()) {
               result = this.skill.preCastCheck(this.cp, this.target);
               sendToClient((Creature)this.getOwner(), this.skill.getSkillID(), result, this.clientRequest);
               if (result == 1) {
                  this.skill.useSkill(true, this.cp, this.target);
               }

               this.setEnd(true);
               ((Creature)this.getOwner()).idle();
            } else {
               if (now - ((Creature)this.getOwner()).getLastAttackTime() < (long)((Creature)this.getOwner()).getSkillManager().getAttackSpeed()) {
                  return;
               }

               result = this.skill.preCastCheck(this.cp, this.target);
               sendToClient((Creature)this.getOwner(), this.skill.getSkillID(), result, this.clientRequest);
               if (result == 1) {
                  this.skill.useSkill(true, this.cp, this.target);
               }

               if (result == 1) {
                  ((Creature)this.getOwner()).setLastAttackTime(now);
                  if (((Creature)this.getOwner()).getType() == 1) {
                     ((Creature)this.getOwner()).idle();
                  } else if (this.target != null && !this.target.isDie()) {
                     ((Creature)this.getOwner()).attack(this.target);
                  } else {
                     ((Creature)this.getOwner()).idle();
                  }
               } else if (((Creature)this.getOwner()).getType() == 1) {
                  ((Creature)this.getOwner()).idle();
               }
            }
         }

      }
   }

   public static void sendToClient(Creature creature, int skillID, int result, boolean clientRequest) {
      if (creature.getType() == 1 && clientRequest) {
         UseSkill.sendToClient((Player)creature, skillID, result);
         if (result != 1 && clientRequest) {
            BottomMessage.pushMessage((Player)creature, result);
         }
      }

   }

   public Status getStatus() {
      return Status.USESKILL;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.USESKILL;
   }
}
