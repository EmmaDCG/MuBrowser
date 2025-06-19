package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.SkillManager;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.io.game.packet.imp.attack.CreaturePositionCorrect;
import com.mu.io.game.packet.imp.attack.FightResult;
import com.mu.io.game.packet.imp.skill.UseSkill;
import com.mu.io.game.packet.imp.sys.BottomMessage;
import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerAttackEvent extends Event {
   public static final int OutofRangeCorrectCount = 10;
   private static Logger logger = LoggerFactory.getLogger(PlayerAttackEvent.class);
   private Creature target = null;
   private boolean firstTime = true;

   public PlayerAttackEvent(Player owner, Creature target) {
      super(owner);
      this.target = target;
   }

   public static boolean canDo(Player player, long now) {
      if (player.operationInLimit(OperationEnum.ATTACK)) {
         return false;
      } else {
         return !player.operationInLimit(OperationEnum.USESKILL);
      }
   }

   public void work(long now) throws Exception {
      if (((Player)this.getOwner()).operationInLimit(OperationEnum.ATTACK)) {
         ((Player)this.getOwner()).idle();
      } else if (!((Player)this.getOwner()).operationInLimit(OperationEnum.USESKILL)) {
         long tmp = now - ((Player)this.getOwner()).getLastAttackTime();
         if (tmp >= (long)((Player)this.getOwner()).getSkillManager().getAttackSpeed()) {
            if (!((Player)this.getOwner()).getSkillManager().isInCommonCoolTime(now)) {
               int result = canAttack((Player)this.getOwner(), this.target);
               if (result != 1) {
                  this.attackResult(result, ((Player)this.getOwner()).getSkillManager().getCommonSkill().getSkillID(), now);
                  ((Player)this.getOwner()).idle();
               } else {
                  SkillManager manager = ((Player)this.getOwner()).getSkillManager();
                  Skill skill = null;
                  Point cp = null;
                  if (((Player)this.getOwner()).isInHanging()) {
                     HashSet attackSkills = ((Player)this.getOwner()).getGameHang().getCurrentAttackSkills();
                     StatEnum conStat = StatEnum.MP;
                     int conValue = 0;
                     Iterator var13 = attackSkills.iterator();

                     label95:
                     while(true) {
                        Skill tmpSkill;
                        do {
                           while(true) {
                              do {
                                 do {
                                    if (!var13.hasNext()) {
                                       if (skill == null) {
                                          skill = ((Player)this.getOwner()).getSkillManager().getCommonSkill();
                                          if (skill.isInCoolTime()) {
                                             return;
                                          }
                                       }
                                       break label95;
                                    }

                                    Integer skillID = (Integer)var13.next();
                                    tmpSkill = manager.getSkill(skillID.intValue());
                                 } while(tmpSkill == null);

                                 result = this.check(tmpSkill);
                              } while(result != 1);

                              if (tmpSkill.getModel().getConsumeStat() == StatEnum.AG) {
                                 break;
                              }

                              if (conStat != StatEnum.AG && conValue <= tmpSkill.getConsumeValue()) {
                                 conValue = tmpSkill.getConsumeValue();
                                 skill = tmpSkill;
                              }
                           }
                        } while(conStat == StatEnum.AG && conValue > tmpSkill.getConsumeValue());

                        conStat = StatEnum.AG;
                        conValue = tmpSkill.getConsumeValue();
                        skill = tmpSkill;
                     }
                  } else {
                     Skill tmpSkill = ((Player)this.getOwner()).getSkillManager().getAutoSkill();
                     if (tmpSkill != null) {
                        result = this.check(tmpSkill);
                     }

                     if (result == 1) {
                        skill = tmpSkill;
                     }
                  }

                  if (skill == null) {
                     skill = ((Player)this.getOwner()).getSkillManager().getCommonSkill();
                  }

                  if (skill.isCommonSkill()) {
                     result = skill.useSkill(false, ((Player)this.getOwner()).getPosition(), this.target);
                  } else {
                     result = 1;
                     cp = this.fillingSkill(skill);
                     skill.useSkill(true, cp, this.target);
                  }

                  if (result == 1) {
                     if (logger.isDebugEnabled()) {
                        logger.debug("使用了技能  {}", skill.getModel().getName());
                     }

                     ((Player)this.getOwner()).setLastAttackTime(System.currentTimeMillis());
                     UseSkill.sendToClient((Player)this.getOwner(), skill.getSkillID(), result);
                  }

                  this.attackResult(result, skill.getSkillID(), now);
                  if (result != 1) {
                     ((Player)this.getOwner()).idle();
                  } else {
                     if (result == 1 && this.target.isDie()) {
                        ((Player)this.getOwner()).idle();
                     }

                  }
               }
            }
         }
      }
   }

   public static int canAttack(Player player, Creature attacked) {
      int result = 1;

      try {
         if (player.isDie() || player.isDestroy()) {
            return 8028;
         }

         if (attacked == null) {
            return 8026;
         }

         if (attacked.isDie()) {
            return 8028;
         }

         if (attacked.isDestroy()) {
            return 8026;
         }

         if (!player.getMap().equals(attacked.getMap())) {
            return 8018;
         }

         result = attacked.canBeAttackedByPlayer(player);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return result;
   }

   public void attackResult(int result, int skillID, long now) {
      int type;
      if (result == 8014 && this.target != null) {
         type = ((Player)this.getOwner()).getOutOfRangeTimes();
         ++type;
         if (type >= 10) {
            Point point = ((Player)this.getOwner()).getActualPosition();
            int x = point.x;
            int y = point.y;
            if (((Player)this.getOwner()).getMap().isBlocked(x, y)) {
               Point p = ((Player)this.getOwner()).getMap().searchFeasiblePoint(x, y);
               x = p.x;
               y = p.y;
               if (logger.isDebugEnabled()) {
                  logger.debug("当前位置异常\t{}", ((Player)this.getOwner()).getMap().getID());
               }
            }

            CreaturePositionCorrect.correntWhenOutofRange((Player)this.getOwner(), this.target, x, y);
            ((Player)this.getOwner()).setOutOfRangeTimes(0);
         }

         ((Player)this.getOwner()).setOutOfRangeTimes(type);
      } else if (this.firstTime && result != 1 && result != 8028) {
         type = this.target == null ? -1 : this.target.getType();
         logger.debug(type + "," + ((Player)this.getOwner()).getSkillManager().isInCommonCoolTime(now) + "愿意 = " + result + ",技能ID = " + skillID + "," + ((Player)this.getOwner()).getSkillManager().getSkill(skillID).getCoolTime());
         BottomMessage.pushMessage((Player)this.getOwner(), result);
         this.firstTime = false;
      }

      FightResult.sendToClient((Player)this.getOwner(), result, skillID);
   }

   private int check(Skill tmpSkill) {
      if (tmpSkill.getLevel() < 1) {
         return 8008;
      } else if (!tmpSkill.isDeBenefiesSkill()) {
         return 8013;
      } else {
         Point cp = this.fillingSkill(tmpSkill);
         int result = tmpSkill.preCastCheck(cp, this.target);
         return result;
      }
   }

   public Point fillingSkill(Skill skill) {
      if (skill != null) {
         if (this.target != null) {
            return this.target.getPosition();
         }

         if (skill.getRange() > 0) {
            return ((Player)this.getOwner()).getPosition();
         }
      }

      return null;
   }

   public Status getStatus() {
      return Status.ATTACK;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }

   public Creature getTarget() {
      return this.target;
   }
}
