package com.mu.game.model.unit;

import com.mu.game.model.item.model.MovementType;
import com.mu.game.model.map.Map;
import com.mu.game.model.packet.CreatureHpMpPacket;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.properties.CreatureProperties;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.CreatureTemplate;
import com.mu.game.model.unit.MapUnit;
import com.mu.game.model.unit.Unit;
import com.mu.game.model.unit.ai.AggroList;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.buff.BuffManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.SkillManager;
import com.mu.game.model.unit.trigger.TriggerManager;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.game.model.unit.unitevent.imp.AttackEvent;
import com.mu.game.model.unit.unitevent.imp.DeathEvent;
import com.mu.game.model.unit.unitevent.imp.UseBuffEvent;
import com.mu.game.model.unit.unitevent.imp.UseSkillEvent;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.attack.CorrectAttributes;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.io.game.packet.imp.map.UnitMove;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;

public abstract class Creature extends MapUnit {

   private AggroList aggroList = null;
   private int hp = 0;
   private int mp = 0;
   private int sd = 0;
   private int ap = 0;
   private int ag = 0;
   private int level = 1;
   protected int atkInterval = 1500;
   protected int attackDistance = 2000;
   protected int minAttackDistance = 60;
   protected boolean canBeAttacked = true;
   protected long dieTime = -1L;
   protected long lastAttackTime = -1L;
   protected Point bornPoint = new Point(-1, -1);
   protected Point bornCenter = null;
   protected boolean isFindWay = false;
   protected boolean isAlreadyDie = false;
   private boolean destroyWhenBeKilled = false;
   private CreatureProperties property = null;
   private SkillManager skillManager = null;
   private BuffManager buffManager = null;
   private TriggerManager triggerManager = null;
   private int templateId;
   private int professionID;
   private int rewardExp = 0;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$stats$StatEnum;


   public Creature(long id, Map map) {
      super(id, map);
      this.property = new CreatureProperties(this);
      this.skillManager = new SkillManager(this);
      this.professionID = 100;
      this.buffManager = new BuffManager(this);
      this.triggerManager = new TriggerManager(this);
   }

   public abstract boolean doAttack(Creature var1, AttackResult var2, boolean var3);

   public abstract int canBeAttackedByPlayer(Player var1);

   public abstract boolean hasAttackedMarkForShow(Player var1);

   public CreatureProperties getProperty() {
      return this.property;
   }

   public boolean isDie() {
      return this.isAlreadyDie || this.getStatus() == Status.DEATH;
   }

   public void startMove(Point[] path, long moveTime) {
      super.startMove(path, moveTime);
      this.getTriggerManager().handleMove();
   }

   public void doWork(Map map, long now) {
      if(this.buffManager != null && !this.buffManager.isDestroy()) {
         this.buffManager.check(now);
      }

      super.doWork(map, now);
   }

   public void addSd(int value) {
      if(value >= 0) {
         this.sd = Math.min(this.getMaxSD(), this.sd + value);
         CreatureHpMpPacket.creatureSdChange(this);
         CorrectAttributes.sendWhenChange(this, StatEnum.SD);
      }
   }

   public void reduceSd(int value) {
      if(value >= 0) {
         this.sd = Math.max(0, this.sd - value);
         CreatureHpMpPacket.creatureSdChange(this);
         CorrectAttributes.sendWhenChange(this, StatEnum.SD);
      }
   }

   public void addAp(int value) {
      if(value >= 0) {
         this.ap = Math.min(this.getMaxAP(), this.ap + value);
         CreatureHpMpPacket.creatureApChange(this);
      }
   }

   public void addAg(int value) {
      if(value >= 0) {
         this.ag = Math.min(this.getMaxAG(), this.ag + value);
         CreatureHpMpPacket.creatureAGChange(this);
      }
   }

   public void reduceAp(int value) {
      if(value >= 0) {
         this.ap = Math.max(0, this.ap - value);
         CreatureHpMpPacket.creatureApChange(this);
      }
   }

   public int getHp() {
      return this.hp;
   }

   public int getMp() {
      return this.mp;
   }

   public void setAlreadyDie(boolean b) {
      this.isAlreadyDie = b;
   }

   public CreatureTemplate getTemplate() {
      return CreatureTemplate.getTemplate(this.templateId);
   }

   public void setTemplateId(int templateId) {
      this.templateId = templateId;
   }

   public int getDieMusic() {
      return -1;
   }

   public int getTemplateId() {
      return this.templateId;
   }

   public int getSd() {
      return this.sd;
   }

   public int getAg() {
      return this.ag;
   }

   public int getAp() {
      return this.ap;
   }

   public void setHp(int hp) {
      this.hp = hp;
   }

   public void setSd(int sd) {
      this.sd = sd;
   }

   public void setAg(int ag) {
      this.ag = ag;
   }

   public void setAp(int ap) {
      this.ap = ap;
   }

   public int increaseHp(int value) {
      if(value <= 0) {
         return 0;
      } else {
         int tmp = this.hp + value;
         if(tmp > this.getMaxHp()) {
            value = this.getMaxHp() - this.hp;
            this.setHp(this.getMaxHp());
         } else if(tmp > 0) {
            this.setHp(tmp);
         }

         this.hpChange();
         return value;
      }
   }

   public int increaseMp(int value) {
      if(value <= 0) {
         return 0;
      } else {
         int tmp = this.mp + value;
         if(tmp > this.getMaxMp()) {
            value = this.getMaxMp() - this.mp;
            this.setMp(this.getMaxMp());
         } else if(tmp > 0) {
            this.setMp(tmp);
         }

         return value;
      }
   }

   public int decreaseHp(Creature attacker, AttackResult result) {
      int r = result.getActualDamage();
      if(r <= 0) {
         return r;
      } else {
         int tmp = this.hp - result.getDamage();
         if(tmp > this.getMaxHp()) {
            tmp = this.getMaxHp();
         }

         if(tmp <= 0) {
            r = this.hp;
            this.setHp(0);
            this.beKilled(attacker, result);
         } else {
            this.setHp(tmp);
         }

         if(result.getSdReduce() > 0) {
            this.reduceSd(result.getSdReduce());
         }

         this.hpChange();
         return r;
      }
   }

   protected void hpChange() {}

   public int decreaseMp(int value) {
      int tmpValue = this.getMp();
      this.setMp(this.mp - value);
      return tmpValue < value?tmpValue:value;
   }

   public void setMp(int mp) {
      this.mp = Math.max(0, mp);
   }

   public int getMaxHp() {
      return this.property.getCurrentStat(StatEnum.MAX_HP);
   }

   public int getHpRecover() {
      return this.property.getCurrentStat(StatEnum.HP_RECOVER);
   }

   public int getMaxMp() {
      return this.property.getCurrentStat(StatEnum.MAX_MP);
   }

   public int getMpRecover() {
      return this.property.getCurrentStat(StatEnum.MP_RECOVER);
   }

   public int getHpRecWKMonster() {
      return this.property.getCurrentStat(StatEnum.HP_REC_KILL_MONSTER);
   }

   public int getMpRecWKMonster() {
      return this.property.getCurrentStat(StatEnum.MP_REC_KILL_MONSTER);
   }

   public int getMaxAG() {
      return this.property.getCurrentStat(StatEnum.MAX_AG);
   }

   public int getAgRecover() {
      return this.property.getCurrentStat(StatEnum.AG_RECOVER);
   }

   public int getMaxAP() {
      return this.property.getCurrentStat(StatEnum.MAX_AP);
   }

   public int getApRecover() {
      return this.property.getCurrentStat(StatEnum.AP_RECOVER);
   }

   public int getMaxSD() {
      return this.property.getCurrentStat(StatEnum.MAX_SD);
   }

   public int getSdRecover() {
      return this.property.getCurrentStat(StatEnum.SD_RECOVER);
   }

   public int getMaxAtk() {
      return this.property.getCurrentStat(StatEnum.ATK_MAX);
   }

   public int getMinAtk() {
      return this.property.getCurrentStat(StatEnum.ATK_MIN);
   }

   public int getDef() {
      return this.property.getCurrentStat(StatEnum.DEF);
   }

   public int getHit() {
      return this.property.getCurrentStat(StatEnum.HIT);
   }

   public int getAbsoluteHit() {
      return this.property.getCurrentStat(StatEnum.HIT_ABSOLUTE);
   }

   public int getAvd() {
      return this.property.getCurrentStat(StatEnum.AVD);
   }

   public int getAbsoluteAvd() {
      return this.property.getCurrentStat(StatEnum.AVD_ABSOLUTE);
   }

   public int getStatValue(StatEnum stat) {
      switch($SWITCH_TABLE$com$mu$game$model$stats$StatEnum()[stat.ordinal()]) {
      case 7:
         return this.getHp();
      case 11:
         return this.getMp();
      case 15:
         return this.getSd();
      case 19:
         return this.getAg();
      case 23:
         return this.getAp();
      case 37:
         return this.getLevel();
      default:
         return this.property.getCurrentStat(stat);
      }
   }

   public int getBasisStatValue(StatEnum stat) {
      return this.property.getBaseStat(stat);
   }

   public void setStatValue(StatEnum stat, int value) {
      this.property.initStat(stat, value);
   }

   public boolean isCanBeAttacked() {
      return this.canBeAttacked;
   }

   public float getSpeed() {
      return (float)this.getStatValue(StatEnum.SPEED) / 100.0F;
   }

   public void setCanBeAttacked(boolean canBeAttacked) {
      this.canBeAttacked = canBeAttacked;
   }

   public long getDieTime() {
      return this.dieTime;
   }

   public void setDieTime(long dieTime) {
      this.dieTime = dieTime;
   }

   public long getLastAttackTime() {
      return this.lastAttackTime;
   }

   public void setLastAttackTime(long lastAttackTime) {
      this.lastAttackTime = lastAttackTime;
   }

   public final int getLevel() {
      return this.level;
   }

   public final void setLevel(int level) {
      this.level = level;
   }

   public Point getBornPoint() {
      return this.bornPoint;
   }

   public void setBornPoint(Point bornPosition) {
      this.bornPoint.x = bornPosition.x;
      this.bornPoint.y = bornPosition.y;
      if(this.bornCenter == null) {
         this.setBornCenter(bornPosition);
      }

   }

   public void setBornCenter(Point p) {
      this.bornCenter = new Point(p);
   }

   public Point getBornCenter() {
      return this.bornCenter;
   }

   public boolean canSee(Player player) {
      return true;
   }

   public void attack(Creature target) {
      this.doStatusEvent(new AttackEvent(this, target));
   }

   public boolean isAttacking() {
      return this.getStatus() == Status.ATTACK;
   }

   public Creature getAttackTarget() {
      if(this.getStatus() == Status.ATTACK) {
         AttackEvent event = (AttackEvent)this.getStatusEvent();
         return event.getTarget();
      } else {
         return null;
      }
   }

   public boolean beAttacked(Creature attacker, AttackResult result) {
      if(!this.isDestroy() && !this.isDie()) {
         if(!this.isCanBeAttacked()) {
            return false;
         } else {
            if(this.isDamageImmunity()) {
               result.setType(5);
            }

            this.addHateByHurt(attacker, result, 1);
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean isDamageImmunity() {
      return !this.isCanBeAttacked() || this.getStatValue(StatEnum.INVINCIBLE) >= 1;
   }

   public void addHateByHurt(Creature attacker, AttackResult result, int attackerHate) {
      this.getAggroList().addHateByHurt(this.getHateTarget(attacker, result), result.getDamage());
      attacker.getAggroList().addHateByHurt(this, attackerHate);
   }

   public Creature getHateTarget(Creature attacker, AttackResult result) {
      return (Creature)(attacker instanceof Pet?((Pet)attacker).getOwner():attacker);
   }

   public void hpReduceForDamage(Creature attacker, AttackResult result) {
      if(!this.isDestroy() && !this.isDie()) {
         this.decreaseHp(attacker, result);
         this.getTriggerManager().handleBeAttacked(attacker, result);
      }
   }

   public void beKilled(Creature attacker, AttackResult result) {
      this.isAlreadyDie = true;
      this.setDieTime(System.currentTimeMillis());
      if(this.buffManager != null) {
         this.buffManager.endBuffWhenDie();
      }

      this.doStatusEvent(new DeathEvent(this));
   }

   public void useSkill(int skillID, Point cp, Creature target, boolean clientRequest) {
      Skill skill = this.getSkillManager().getSkill(skillID);
      if(skill != null) {
         this.doStatusEvent(new UseSkillEvent(skill, cp, target, clientRequest));
      }

      skill = null;
   }

   public boolean isSprint() {
      return this.getStatus() == Status.Sprint;
   }

   public void useBuff(int buffID, int level, Object ... objs) {
      this.addRepeatImmediateEvent(new UseBuffEvent(this, buffID, level, objs));
   }

   public boolean destroyWhenBeKilled() {
      return this.destroyWhenBeKilled;
   }

   public void setDestroyWhenBeKilled(boolean b) {
      this.destroyWhenBeKilled = true;
   }

   public int getMovementType() {
      return MovementType.None.getType();
   }

   public void handleUseSkill(boolean debenify) {}

   public void toNewArea(Rectangle newArea, Rectangle oldArea) {
      if(oldArea != null) {
         UnitMove mm = null;
         Point[] ru;
         if(this.isMoving()) {
            ru = this.getMovePath();
            if(ru != null) {
               mm = new UnitMove(this, ru);
            }
         }

         RemoveUnit ru1 = new RemoveUnit(new Unit[]{this});
         Iterator it = this.getMap().getPlayerMap().values().iterator();

         while(it.hasNext()) {
            Player p = (Player)it.next();
            if(p.isEnterMap() && !p.isDestroy()) {
               Point point = p.getPosition();
               if(point != null) {
                  boolean inNewArea = newArea.contains(point);
                  boolean inOldArea = oldArea.contains(point);
                  if(inNewArea && !inOldArea) {
                     WriteOnlyPacket aroundself = this.createAroundSelfPacket(p);
                     p.writePacket(aroundself);
                     aroundself.destroy();
                     aroundself = null;
                     if(mm != null) {
                        p.writePacket(mm);
                     }
                  } else if(inOldArea && !inNewArea) {
                     p.writePacket(ru1);
                  }
               }
            }
         }

         if(mm != null) {
            mm.destroy();
            mm = null;
         }

         ru1.destroy();
         ru = null;
      }
   }

   public void revival() {
      this.setHp(this.getMaxHp());
      this.setMp(this.getMaxMp());
      this.setDestroy(false);
      this.setAlreadyDie(false);
      this.idle();
   }

   public void fullResume() {
      this.setHp(this.getMaxHp());
      this.setMp(this.getMaxMp());
   }

   public void destroy() {
      super.destroy();
      if(this.property != null) {
         this.property.destroy();
         this.property = null;
      }

      if(this.buffManager != null) {
         this.buffManager.destroy();
         this.buffManager = null;
      }

      if(this.skillManager != null) {
         this.skillManager.destroy();
         this.skillManager = null;
      }

      if(this.triggerManager != null) {
         this.triggerManager.destroy();
         this.triggerManager = null;
      }

   }

   public final boolean isFindWay() {
      return this.isFindWay;
   }

   public final void setFindWay(boolean isFindWay) {
      this.isFindWay = isFindWay;
   }

   public final int getAttackDistance() {
      return this.attackDistance;
   }

   public final void setAttackDistance(int attackDistance) {
      this.attackDistance = attackDistance;
   }

   public final int getMinAttackDistance() {
      return this.minAttackDistance;
   }

   public final void setMinAttackDistance(int minAttackDistance) {
      this.minAttackDistance = minAttackDistance;
   }

   public final AggroList getAggroList() {
      if(this.aggroList == null) {
         this.aggroList = new AggroList(this);
      }

      return this.aggroList;
   }

   public SkillManager getSkillManager() {
      return this.skillManager;
   }

   public int getProfessionID() {
      return this.professionID;
   }

   public void setProfessionID(int professionID) {
      this.professionID = professionID;
   }

   public int getRewardExp() {
      return this.rewardExp;
   }

   public void setRewardExp(int rewardExp) {
      this.rewardExp = rewardExp;
   }

   public BuffManager getBuffManager() {
      return this.buffManager;
   }

   public TriggerManager getTriggerManager() {
      return this.triggerManager;
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$stats$StatEnum() {
      if($SWITCH_TABLE$com$mu$game$model$stats$StatEnum != null) {
         return $SWITCH_TABLE$com$mu$game$model$stats$StatEnum;
      } else {
         int[] var0 = new int[StatEnum.values().length];

         try {
            var0[StatEnum.ABSORB_AG.ordinal()] = 138;
         } catch (NoSuchFieldError var146) {
            ;
         }

         try {
            var0[StatEnum.ABSORB_HP.ordinal()] = 136;
         } catch (NoSuchFieldError var145) {
            ;
         }

         try {
            var0[StatEnum.ABSORB_MP.ordinal()] = 134;
         } catch (NoSuchFieldError var144) {
            ;
         }

         try {
            var0[StatEnum.ABSORB_SD.ordinal()] = 140;
         } catch (NoSuchFieldError var143) {
            ;
         }

         try {
            var0[StatEnum.AG.ordinal()] = 19;
         } catch (NoSuchFieldError var142) {
            ;
         }

         try {
            var0[StatEnum.AG_RECOVER.ordinal()] = 21;
         } catch (NoSuchFieldError var141) {
            ;
         }

         try {
            var0[StatEnum.AG_REC_KILL_MONSTER.ordinal()] = 22;
         } catch (NoSuchFieldError var140) {
            ;
         }

         try {
            var0[StatEnum.ALL_BASIS.ordinal()] = 6;
         } catch (NoSuchFieldError var139) {
            ;
         }

         try {
            var0[StatEnum.AP.ordinal()] = 23;
         } catch (NoSuchFieldError var138) {
            ;
         }

         try {
            var0[StatEnum.AP_RECOVER.ordinal()] = 25;
         } catch (NoSuchFieldError var137) {
            ;
         }

         try {
            var0[StatEnum.ATK.ordinal()] = 26;
         } catch (NoSuchFieldError var136) {
            ;
         }

         try {
            var0[StatEnum.ATK_AG_REC_RATE.ordinal()] = 81;
         } catch (NoSuchFieldError var135) {
            ;
         }

         try {
            var0[StatEnum.ATK_EXCELLENT_DAM.ordinal()] = 90;
         } catch (NoSuchFieldError var134) {
            ;
         }

         try {
            var0[StatEnum.ATK_EXCELLENT_RATE.ordinal()] = 89;
         } catch (NoSuchFieldError var133) {
            ;
         }

         try {
            var0[StatEnum.ATK_EXCELLENT_RES.ordinal()] = 91;
         } catch (NoSuchFieldError var132) {
            ;
         }

         try {
            var0[StatEnum.ATK_FATAL_DAM.ordinal()] = 97;
         } catch (NoSuchFieldError var131) {
            ;
         }

         try {
            var0[StatEnum.ATK_FATAL_RATE.ordinal()] = 95;
         } catch (NoSuchFieldError var130) {
            ;
         }

         try {
            var0[StatEnum.ATK_FATAL_RES.ordinal()] = 96;
         } catch (NoSuchFieldError var129) {
            ;
         }

         try {
            var0[StatEnum.ATK_LUCKY_DAM.ordinal()] = 93;
         } catch (NoSuchFieldError var128) {
            ;
         }

         try {
            var0[StatEnum.ATK_LUCKY_RATE.ordinal()] = 92;
         } catch (NoSuchFieldError var127) {
            ;
         }

         try {
            var0[StatEnum.ATK_LUCKY_RES.ordinal()] = 94;
         } catch (NoSuchFieldError var126) {
            ;
         }

         try {
            var0[StatEnum.ATK_MAX.ordinal()] = 28;
         } catch (NoSuchFieldError var125) {
            ;
         }

         try {
            var0[StatEnum.ATK_MIN.ordinal()] = 27;
         } catch (NoSuchFieldError var124) {
            ;
         }

         try {
            var0[StatEnum.ATK_MP_REC_RATE.ordinal()] = 83;
         } catch (NoSuchFieldError var123) {
            ;
         }

         try {
            var0[StatEnum.ATK_SPEED.ordinal()] = 67;
         } catch (NoSuchFieldError var122) {
            ;
         }

         try {
            var0[StatEnum.ATTACK_CAPABILITY.ordinal()] = 117;
         } catch (NoSuchFieldError var121) {
            ;
         }

         try {
            var0[StatEnum.AVD.ordinal()] = 33;
         } catch (NoSuchFieldError var120) {
            ;
         }

         try {
            var0[StatEnum.AVD_ABSOLUTE.ordinal()] = 34;
         } catch (NoSuchFieldError var119) {
            ;
         }

         try {
            var0[StatEnum.All_Points.ordinal()] = 56;
         } catch (NoSuchFieldError var118) {
            ;
         }

         try {
            var0[StatEnum.BEINJURED_HP_REC_RATE.ordinal()] = 85;
         } catch (NoSuchFieldError var117) {
            ;
         }

         try {
            var0[StatEnum.BEINJURED_SD_REC_RATE.ordinal()] = 87;
         } catch (NoSuchFieldError var116) {
            ;
         }

         try {
            var0[StatEnum.BIND_INGOT.ordinal()] = 44;
         } catch (NoSuchFieldError var115) {
            ;
         }

         try {
            var0[StatEnum.BIND_MONEY.ordinal()] = 42;
         } catch (NoSuchFieldError var114) {
            ;
         }

         try {
            var0[StatEnum.CD_BONUS.ordinal()] = 102;
         } catch (NoSuchFieldError var113) {
            ;
         }

         try {
            var0[StatEnum.CON.ordinal()] = 5;
         } catch (NoSuchFieldError var112) {
            ;
         }

         try {
            var0[StatEnum.Contribution.ordinal()] = 63;
         } catch (NoSuchFieldError var111) {
            ;
         }

         try {
            var0[StatEnum.DAM_ABSORB.ordinal()] = 69;
         } catch (NoSuchFieldError var110) {
            ;
         }

         try {
            var0[StatEnum.DAM_FORCE.ordinal()] = 80;
         } catch (NoSuchFieldError var109) {
            ;
         }

         try {
            var0[StatEnum.DAM_IGNORE.ordinal()] = 78;
         } catch (NoSuchFieldError var108) {
            ;
         }

         try {
            var0[StatEnum.DAM_PVP.ordinal()] = 77;
         } catch (NoSuchFieldError var107) {
            ;
         }

         try {
            var0[StatEnum.DAM_REDUCE.ordinal()] = 70;
         } catch (NoSuchFieldError var106) {
            ;
         }

         try {
            var0[StatEnum.DAM_REFLECTION.ordinal()] = 76;
         } catch (NoSuchFieldError var105) {
            ;
         }

         try {
            var0[StatEnum.DAM_REFLECTION_PRO.ordinal()] = 75;
         } catch (NoSuchFieldError var104) {
            ;
         }

         try {
            var0[StatEnum.DAM_STRENGTHEN.ordinal()] = 71;
         } catch (NoSuchFieldError var103) {
            ;
         }

         try {
            var0[StatEnum.DEF.ordinal()] = 29;
         } catch (NoSuchFieldError var102) {
            ;
         }

         try {
            var0[StatEnum.DEFENCE_CAPABILITY.ordinal()] = 118;
         } catch (NoSuchFieldError var101) {
            ;
         }

         try {
            var0[StatEnum.DEF_STRENGTH.ordinal()] = 30;
         } catch (NoSuchFieldError var100) {
            ;
         }

         try {
            var0[StatEnum.DEX.ordinal()] = 3;
         } catch (NoSuchFieldError var99) {
            ;
         }

         try {
            var0[StatEnum.DISARM.ordinal()] = 130;
         } catch (NoSuchFieldError var98) {
            ;
         }

         try {
            var0[StatEnum.DOMINEERING.ordinal()] = 65;
         } catch (NoSuchFieldError var97) {
            ;
         }

         try {
            var0[StatEnum.DOUBLE_ATK.ordinal()] = 68;
         } catch (NoSuchFieldError var96) {
            ;
         }

         try {
            var0[StatEnum.DROPRATE.ordinal()] = 62;
         } catch (NoSuchFieldError var95) {
            ;
         }

         try {
            var0[StatEnum.EVALUATION.ordinal()] = 57;
         } catch (NoSuchFieldError var94) {
            ;
         }

         try {
            var0[StatEnum.EVIL.ordinal()] = 48;
         } catch (NoSuchFieldError var93) {
            ;
         }

         try {
            var0[StatEnum.EXP.ordinal()] = 38;
         } catch (NoSuchFieldError var92) {
            ;
         }

         try {
            var0[StatEnum.EXP_BONUS.ordinal()] = 40;
         } catch (NoSuchFieldError var91) {
            ;
         }

         try {
            var0[StatEnum.FROST.ordinal()] = 122;
         } catch (NoSuchFieldError var90) {
            ;
         }

         try {
            var0[StatEnum.HIT.ordinal()] = 31;
         } catch (NoSuchFieldError var89) {
            ;
         }

         try {
            var0[StatEnum.HIT_ABSOLUTE.ordinal()] = 32;
         } catch (NoSuchFieldError var88) {
            ;
         }

         try {
            var0[StatEnum.HP.ordinal()] = 7;
         } catch (NoSuchFieldError var87) {
            ;
         }

         try {
            var0[StatEnum.HP_RECOVER.ordinal()] = 9;
         } catch (NoSuchFieldError var86) {
            ;
         }

         try {
            var0[StatEnum.HP_REC_KILL_MONSTER.ordinal()] = 10;
         } catch (NoSuchFieldError var85) {
            ;
         }

         try {
            var0[StatEnum.HisContribution.ordinal()] = 64;
         } catch (NoSuchFieldError var84) {
            ;
         }

         try {
            var0[StatEnum.IGNORE_DEF.ordinal()] = 79;
         } catch (NoSuchFieldError var83) {
            ;
         }

         try {
            var0[StatEnum.IGNORE_DEF_PRO.ordinal()] = 73;
         } catch (NoSuchFieldError var82) {
            ;
         }

         try {
            var0[StatEnum.IGNORE_SD_PRO.ordinal()] = 74;
         } catch (NoSuchFieldError var81) {
            ;
         }

         try {
            var0[StatEnum.INGOT.ordinal()] = 43;
         } catch (NoSuchFieldError var80) {
            ;
         }

         try {
            var0[StatEnum.INT.ordinal()] = 4;
         } catch (NoSuchFieldError var79) {
            ;
         }

         try {
            var0[StatEnum.INVINCIBLE.ordinal()] = 142;
         } catch (NoSuchFieldError var78) {
            ;
         }

         try {
            var0[StatEnum.ITEM_USERLEVEL_DOWN.ordinal()] = 54;
         } catch (NoSuchFieldError var77) {
            ;
         }

         try {
            var0[StatEnum.LEVEL.ordinal()] = 37;
         } catch (NoSuchFieldError var76) {
            ;
         }

         try {
            var0[StatEnum.LEVELGAP.ordinal()] = 61;
         } catch (NoSuchFieldError var75) {
            ;
         }

         try {
            var0[StatEnum.LUCKY.ordinal()] = 46;
         } catch (NoSuchFieldError var74) {
            ;
         }

         try {
            var0[StatEnum.MASTER_SKILL.ordinal()] = 58;
         } catch (NoSuchFieldError var73) {
            ;
         }

         try {
            var0[StatEnum.MAX_AG.ordinal()] = 20;
         } catch (NoSuchFieldError var72) {
            ;
         }

         try {
            var0[StatEnum.MAX_AP.ordinal()] = 24;
         } catch (NoSuchFieldError var71) {
            ;
         }

         try {
            var0[StatEnum.MAX_EXP.ordinal()] = 39;
         } catch (NoSuchFieldError var70) {
            ;
         }

         try {
            var0[StatEnum.MAX_HP.ordinal()] = 8;
         } catch (NoSuchFieldError var69) {
            ;
         }

         try {
            var0[StatEnum.MAX_MP.ordinal()] = 12;
         } catch (NoSuchFieldError var68) {
            ;
         }

         try {
            var0[StatEnum.MAX_SD.ordinal()] = 16;
         } catch (NoSuchFieldError var67) {
            ;
         }

         try {
            var0[StatEnum.MONEY.ordinal()] = 41;
         } catch (NoSuchFieldError var66) {
            ;
         }

         try {
            var0[StatEnum.MONEY_ADD_WKM.ordinal()] = 145;
         } catch (NoSuchFieldError var65) {
            ;
         }

         try {
            var0[StatEnum.MP.ordinal()] = 11;
         } catch (NoSuchFieldError var64) {
            ;
         }

         try {
            var0[StatEnum.MP_RECOVER.ordinal()] = 13;
         } catch (NoSuchFieldError var63) {
            ;
         }

         try {
            var0[StatEnum.MP_REC_KILL_MONSTER.ordinal()] = 14;
         } catch (NoSuchFieldError var62) {
            ;
         }

         try {
            var0[StatEnum.None.ordinal()] = 1;
         } catch (NoSuchFieldError var61) {
            ;
         }

         try {
            var0[StatEnum.PARALYSIS.ordinal()] = 126;
         } catch (NoSuchFieldError var60) {
            ;
         }

         try {
            var0[StatEnum.PERCENT_ATK_AG_REC.ordinal()] = 82;
         } catch (NoSuchFieldError var59) {
            ;
         }

         try {
            var0[StatEnum.PERCENT_ATK_MP_REC.ordinal()] = 84;
         } catch (NoSuchFieldError var58) {
            ;
         }

         try {
            var0[StatEnum.PERCENT_BEINJURED_HP_REC.ordinal()] = 86;
         } catch (NoSuchFieldError var57) {
            ;
         }

         try {
            var0[StatEnum.PERCENT_BEINJURED_SD_REC.ordinal()] = 88;
         } catch (NoSuchFieldError var56) {
            ;
         }

         try {
            var0[StatEnum.PETRIFICATION.ordinal()] = 124;
         } catch (NoSuchFieldError var55) {
            ;
         }

         try {
            var0[StatEnum.PKMODE.ordinal()] = 55;
         } catch (NoSuchFieldError var54) {
            ;
         }

         try {
            var0[StatEnum.POISONING.ordinal()] = 120;
         } catch (NoSuchFieldError var53) {
            ;
         }

         try {
            var0[StatEnum.POISONING_ATK_HURT.ordinal()] = 111;
         } catch (NoSuchFieldError var52) {
            ;
         }

         try {
            var0[StatEnum.POTENTIAL.ordinal()] = 47;
         } catch (NoSuchFieldError var51) {
            ;
         }

         try {
            var0[StatEnum.PROBABILITY.ordinal()] = 49;
         } catch (NoSuchFieldError var50) {
            ;
         }

         try {
            var0[StatEnum.REDEEM_POINTS.ordinal()] = 53;
         } catch (NoSuchFieldError var49) {
            ;
         }

         try {
            var0[StatEnum.RESURRENTION.ordinal()] = 128;
         } catch (NoSuchFieldError var48) {
            ;
         }

         try {
            var0[StatEnum.RES_ABSORB_AG.ordinal()] = 139;
         } catch (NoSuchFieldError var47) {
            ;
         }

         try {
            var0[StatEnum.RES_ABSORB_HP.ordinal()] = 137;
         } catch (NoSuchFieldError var46) {
            ;
         }

         try {
            var0[StatEnum.RES_ABSORB_MP.ordinal()] = 135;
         } catch (NoSuchFieldError var45) {
            ;
         }

         try {
            var0[StatEnum.RES_ABSORB_SD.ordinal()] = 141;
         } catch (NoSuchFieldError var44) {
            ;
         }

         try {
            var0[StatEnum.RES_DISARM.ordinal()] = 131;
         } catch (NoSuchFieldError var43) {
            ;
         }

         try {
            var0[StatEnum.RES_FROST.ordinal()] = 123;
         } catch (NoSuchFieldError var42) {
            ;
         }

         try {
            var0[StatEnum.RES_PARALYSIS.ordinal()] = 127;
         } catch (NoSuchFieldError var41) {
            ;
         }

         try {
            var0[StatEnum.RES_PETRIFICATION.ordinal()] = 125;
         } catch (NoSuchFieldError var40) {
            ;
         }

         try {
            var0[StatEnum.RES_POISONING.ordinal()] = 121;
         } catch (NoSuchFieldError var39) {
            ;
         }

         try {
            var0[StatEnum.RES_RESURRENTION.ordinal()] = 129;
         } catch (NoSuchFieldError var38) {
            ;
         }

         try {
            var0[StatEnum.RES_SACKED.ordinal()] = 133;
         } catch (NoSuchFieldError var37) {
            ;
         }

         try {
            var0[StatEnum.RES_WIND.ordinal()] = 144;
         } catch (NoSuchFieldError var36) {
            ;
         }

         try {
            var0[StatEnum.RNG.ordinal()] = 66;
         } catch (NoSuchFieldError var35) {
            ;
         }

         try {
            var0[StatEnum.SACKED.ordinal()] = 132;
         } catch (NoSuchFieldError var34) {
            ;
         }

         try {
            var0[StatEnum.SD.ordinal()] = 15;
         } catch (NoSuchFieldError var33) {
            ;
         }

         try {
            var0[StatEnum.SD_RECOVER.ordinal()] = 17;
         } catch (NoSuchFieldError var32) {
            ;
         }

         try {
            var0[StatEnum.SD_REC_KILL_MONSTER.ordinal()] = 18;
         } catch (NoSuchFieldError var31) {
            ;
         }

         try {
            var0[StatEnum.SD_REDUCTION.ordinal()] = 72;
         } catch (NoSuchFieldError var30) {
            ;
         }

         try {
            var0[StatEnum.SKILL_AG_REDUCE.ordinal()] = 100;
         } catch (NoSuchFieldError var29) {
            ;
         }

         try {
            var0[StatEnum.SKILL_ATK.ordinal()] = 98;
         } catch (NoSuchFieldError var28) {
            ;
         }

         try {
            var0[StatEnum.SKILL_CASTTIME.ordinal()] = 105;
         } catch (NoSuchFieldError var27) {
            ;
         }

         try {
            var0[StatEnum.SKILL_CD.ordinal()] = 101;
         } catch (NoSuchFieldError var26) {
            ;
         }

         try {
            var0[StatEnum.SKILL_COEFFICIENT.ordinal()] = 107;
         } catch (NoSuchFieldError var25) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA3.ordinal()] = 114;
         } catch (NoSuchFieldError var24) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA4.ordinal()] = 115;
         } catch (NoSuchFieldError var23) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA5.ordinal()] = 116;
         } catch (NoSuchFieldError var22) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA_1.ordinal()] = 112;
         } catch (NoSuchFieldError var21) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA_2.ordinal()] = 113;
         } catch (NoSuchFieldError var20) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DEGREE.ordinal()] = 109;
         } catch (NoSuchFieldError var19) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DISTANCE.ordinal()] = 108;
         } catch (NoSuchFieldError var18) {
            ;
         }

         try {
            var0[StatEnum.SKILL_EFFECT_NUMBER.ordinal()] = 103;
         } catch (NoSuchFieldError var17) {
            ;
         }

         try {
            var0[StatEnum.SKILL_MP_REDUCE.ordinal()] = 99;
         } catch (NoSuchFieldError var16) {
            ;
         }

         try {
            var0[StatEnum.SKILL_PASSIVE_STAT.ordinal()] = 146;
         } catch (NoSuchFieldError var15) {
            ;
         }

         try {
            var0[StatEnum.SKILL_RANGE.ordinal()] = 104;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            var0[StatEnum.SKILL_WEAPON_HURT.ordinal()] = 110;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            var0[StatEnum.SPEED.ordinal()] = 45;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            var0[StatEnum.STR.ordinal()] = 2;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            var0[StatEnum.STRENGTH_LUCKY.ordinal()] = 50;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            var0[StatEnum.STRENGTH_NOBACK.ordinal()] = 51;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            var0[StatEnum.STRENGTH_NODESTROY.ordinal()] = 52;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            var0[StatEnum.TIME.ordinal()] = 106;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            var0[StatEnum.TRIGGER.ordinal()] = 119;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[StatEnum.WEAPON_MAX_ATK.ordinal()] = 36;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[StatEnum.WEAPON_MIN_ATK.ordinal()] = 35;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[StatEnum.WIND.ordinal()] = 143;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[StatEnum.WORLDLEVEL.ordinal()] = 60;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[StatEnum.ZHUIJIA.ordinal()] = 59;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$stats$StatEnum = var0;
         return var0;
      }
   }
}
