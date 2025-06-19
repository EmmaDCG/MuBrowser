package com.mu.game.model.unit.skill;

import com.mu.config.Constant;
import com.mu.game.model.packet.SkillPacketService;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.action.SkillAction;
import com.mu.game.model.unit.skill.condition.Condition;
import com.mu.game.model.unit.skill.condition.ConditionManager;
import com.mu.game.model.unit.skill.consume.Consume;
import com.mu.game.model.unit.skill.levelData.SkillLevelData;
import com.mu.game.model.unit.skill.model.ProfessionSkills;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.game.model.unit.skill.model.SkillMovement;
import java.awt.Point;
import java.awt.Shape;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public abstract class Skill {
   public static final int Skill_Quality = 100;
   private int skillID;
   private int level;
   protected Creature owner;
   private int coolTime = 0;
   private int range = 0;
   private int distance = 0;
   private int maxCount = 0;
   protected long nextThawTime;
   private int rate = 0;
   private int quality = 100;
   private boolean selected;
   private float skillCorrection = 1.0F;
   private float skillFactor = 1.0F;
   private int fixedDamage;
   private int passiveConsume;
   private int maxPassiveConsume;
   public boolean hasCheck = false;
   private boolean isDestroy = false;
   private int consumeValue = 0;
   private int skillStep = 100;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$stats$StatEnum;

   public Skill(int skillID, int level, Creature owner) {
      this.skillID = skillID;
      this.level = level;
      this.owner = owner;
   }

   public abstract int preCastCheck(Point var1, Creature var2);

   protected abstract void startEffect(Point var1, Creature var2);

   public abstract boolean isSprint();

   public void stop() {
   }

   public boolean isDeBenefiesSkill() {
      return !this.getModel().isGain();
   }

   public void init() {
      SkillModel model = this.getModel();
      this.coolTime = model.getCoolTime();
      this.range = model.getRange();
      this.distance = model.getDistance();
      this.maxCount = model.getEffectMaxCount();
      SkillLevelData levelData = SkillLevelData.getLevelData(this.skillID, this.getLevel());
      if (levelData != null) {
         this.coolTime = levelData.getCoolTime();
         this.range = levelData.getRangeAdd();
         this.distance = levelData.getDistance();
         this.maxCount = levelData.getEffectCount();
         this.rate = levelData.getRate();
         this.skillCorrection = levelData.getSkillCorrection();
         this.skillFactor = levelData.getSkillFactor();
         this.fixedDamage = levelData.getStatVlaue(StatEnum.DAM_IGNORE, StatModifyPriority.ADD);
         this.maxPassiveConsume = levelData.getPassiveConsume();
         this.skillStep = 100;
      }

      this.setDefaultConsume();
      float cdRate = this.getOwner().getSkillManager().getCdRedueceRate();
      if (cdRate != 0.0F) {
         int tmpCoolTime = this.coolTime;
         this.coolTime = (int)((double)this.coolTime * (1.0D - (double)cdRate));
         this.coolTime = Math.max(0, this.coolTime);
         if (tmpCoolTime > 0 && this.coolTime > 0) {
            this.skillStep = (int)(1.0D * (double)tmpCoolTime / (double)this.coolTime * 100.0D);
         }
      }

   }

   public int useSkill(boolean hasCheck, Point centPoint, Creature effected) {
      this.hasCheck = hasCheck;
      SkillModel model = this.getModel();
      if (model == null) {
         this.stop();
         return 8007;
      } else {
         if (!hasCheck) {
            int result = this.preCastCheck(centPoint, effected);
            if (result != 1) {
               this.stop();
               return result;
            }
         }

         this.getOwner().handleUseSkill(this.isDeBenefiesSkill());
         if (this.getModel().getStatusType() == 1) {
            this.setCoolDown(model.isPublicCoolTime());
         }

         this.consume();
         this.startEffect(centPoint, effected);
         this.stop();
         model = null;
         this.getOwner().getTriggerManager().handleUseSkill(this);
         return 1;
      }
   }

   public int commonCheck() {
      if (this.getLevel() < 1) {
         return 8008;
      } else if (this.isInCoolTime()) {
         return 8009;
      } else {
         this.isSprint();
         return 1;
      }
   }

   public boolean isInCoolTime() {
      long now = System.currentTimeMillis();
      if (this.nextThawTime > now) {
         return true;
      } else {
         return this.owner.getSkillManager().isSkillDisabled(this.skillID, now);
      }
   }

   public int getRemainTime() {
      return this.getNextThawTime() > System.currentTimeMillis() ? (int)(this.getNextThawTime() - System.currentTimeMillis()) : 0;
   }

   protected void setCoolDown(boolean occuPublicTime) {
      long now = System.currentTimeMillis();
      this.nextThawTime = now + (long)this.getCoolTime();
      this.owner.getSkillManager().setCoolDown(now, occuPublicTime);
   }

   public void consume() {
      List conList = ConditionManager.getConsume(this.skillID, this.level);
      if (conList != null && conList.size() > 0) {
         Iterator var3 = conList.iterator();

         while(var3.hasNext()) {
            Consume con = (Consume)var3.next();
            con.consumed(this);
         }
      }

      conList = null;
   }

   public int useCondition() {
      int result = 1;
      HashMap conMap = ConditionManager.getUseCondition(this.skillID, this.getLevel());
      if (conMap != null && conMap.size() > 0) {
         Iterator var4 = conMap.values().iterator();

         while(var4.hasNext()) {
            Condition con = (Condition)var4.next();
            result = con.verify(this);
            if (result != 1) {
               return result;
            }
         }
      }

      conMap = null;
      return result;
   }

   private void setDefaultConsume() {
      this.consumeValue = 0;
      HashMap conMap = ConditionManager.getUseCondition(this.skillID, this.getLevel());
      if (conMap != null && conMap.size() > 0) {
         Iterator var3 = conMap.values().iterator();

         while(var3.hasNext()) {
            Condition con = (Condition)var3.next();
            switch(con.getType()) {
            case 2:
            case 3:
               this.consumeValue = con.getConsumeValue();
            }
         }
      }

   }

   public int actionCheck(Creature effected, Point specifyPoint) {
      SkillAction action = this.getModel().getAction();
      return action != null ? action.preCheck(this, effected, specifyPoint) : 1;
   }

   public void checkTarget(Shape shape) {
   }

   public List getDynamicData(int skillLevel) {
      List dyDatas = new ArrayList();
      SkillModel sm = this.getModel();
      List order = sm.getDataOrder();
      if (skillLevel < 1) {
         skillLevel = 1;
      }

      BigDecimal bg = null;
      SkillAdditionalProperty ap = this.getProperty();
      SkillLevelData levelData = SkillLevelData.getLevelData(this.skillID, skillLevel);
      double value;
      if (order != null && levelData != null) {
         for(Iterator var9 = order.iterator(); var9.hasNext(); dyDatas.add(value)) {
            StatEnum stat = (StatEnum)var9.next();
            value = 0.0D;
            switch($SWITCH_TABLE$com$mu$game$model$stats$StatEnum()[stat.ordinal()]) {
            case 11:
               value = (double)levelData.getMpConsume();
               if (ap != null) {
                  value = (double)ap.getValue(StatEnum.MP, StatModifyPriority.ADD, (int)value);
               }
               break;
            case 19:
               value = (double)levelData.getAGConsume();
               if (ap != null) {
                  value = (double)ap.getValue(StatEnum.AG, StatModifyPriority.ADD, (int)value);
               }
               break;
            case 41:
               value = (double)levelData.getMoney();
               break;
            case 49:
               value = (double)levelData.getRate();
               value /= 1000.0D;
               break;
            case 101:
               value = (double)levelData.getCoolTime();
               break;
            case 103:
               value = (double)levelData.getEffectCount();
               if (ap != null) {
                  value = (double)ap.getValue(StatEnum.SKILL_EFFECT_NUMBER, StatModifyPriority.ADD, (int)value);
               }
               break;
            case 104:
               value = (double)levelData.getRangeAdd();
               break;
            case 106:
               value = (double)levelData.getBuffTime();
               bg = new BigDecimal(value / 1000.0D);
               value = bg.setScale(1, 4).doubleValue();
               break;
            case 110:
               value = (double)(levelData.getSkillCorrection() * 100000.0F / 1000.0F);
               bg = new BigDecimal(value);
               value = bg.setScale(2, 4).doubleValue();
               break;
            default:
               value = this.getOtherValue(levelData, stat, ap);
            }

            if (value < 0.0D) {
               value = -value;
            }
         }
      }

      sm = null;
      return dyDatas;
   }

   protected double getOtherValue(SkillLevelData levelData, StatEnum stat, SkillAdditionalProperty ap) {
      double value = 0.0D;
      List modifies = levelData.getStats();
      Iterator var8 = modifies.iterator();

      while(var8.hasNext()) {
         FinalModify modify = (FinalModify)var8.next();
         if (modify.getStat() == stat) {
            value = (double)modify.getValue();
            if (ap != null) {
               value = (double)ap.getValue(modify.getStat(), modify.getPriority(), (int)value);
            }

            if (modify.isShowPercent()) {
               BigDecimal bg = new BigDecimal(value / 1000.0D);
               value = bg.setScale(1, 4).doubleValue();
            }
            break;
         }
      }

      return value;
   }

   public boolean isReachMaxPassiveConsume() {
      if (this.level == 0) {
         return true;
      } else {
         return this.passiveConsume >= this.maxPassiveConsume;
      }
   }

   public void addPassiveConsume(int addValue) {
      long maxTmp = 0L + (long)this.getPassiveConsume() + (long)addValue;
      maxTmp = Math.min(maxTmp, 2147483647L);
      this.setPassiveConsume((int)maxTmp);
      if (this.getOwner().getType() == 1) {
         SkillPacketService.noticeGatewayUpdateSkill((Player)this.getOwner(), this, 3);
      }

   }

   public void delPassiveConsume(int delValue) {
      int tmpValue = this.getPassiveConsume() - delValue;
      tmpValue = Math.max(0, tmpValue);
      this.setPassiveConsume(tmpValue);
   }

   public boolean hasInEffectType(Creature creature) {
      HashSet effectTypes = this.getModel().getEffectedType();
      return effectTypes.contains(Integer.valueOf(0)) || effectTypes.contains(creature.getType());
   }

   public boolean hasInEffectType(int creatureType) {
      HashSet effectTypes = this.getModel().getEffectedType();
      return effectTypes.contains(Integer.valueOf(0)) || effectTypes.contains(creatureType);
   }

   public int getCoolTime() {
      SkillAdditionalProperty property = this.getProperty();
      return property != null ? property.getValue(StatEnum.SKILL_CD, StatModifyPriority.ADD, this.coolTime) : this.coolTime;
   }

   public int getRange() {
      SkillAdditionalProperty property = this.getProperty();
      return property != null ? property.getValue(StatEnum.SKILL_RANGE, StatModifyPriority.ADD, this.range) : this.range;
   }

   public int getMaxCount() {
      SkillAdditionalProperty property = this.getProperty();
      return property != null ? property.getValue(StatEnum.SKILL_EFFECT_NUMBER, StatModifyPriority.ADD, this.maxCount) : this.maxCount;
   }

   public int getMpConsume(int value) {
      int reduce = this.getMpReduce(value);
      value -= reduce;
      value = Math.max(0, value);
      return value;
   }

   public int getMpReduce(int value) {
      SkillAdditionalProperty property = this.getProperty();
      return property == null ? 0 : Constant.getPercentValue(value, property.getValue(StatEnum.SKILL_MP_REDUCE, StatModifyPriority.ADD, 0));
   }

   public int getAgConsume(int value) {
      int reduce = this.getAgReduce(value);
      value -= reduce;
      value = Math.max(0, value);
      return value;
   }

   public int getAgReduce(int value) {
      SkillAdditionalProperty property = this.getProperty();
      return property == null ? 0 : Constant.getPercentValue(value, property.getValue(StatEnum.SKILL_AG_REDUCE, StatModifyPriority.ADD, 0));
   }

   public int getConsumByStat(StatEnum stat, int value) {
      switch($SWITCH_TABLE$com$mu$game$model$stats$StatEnum()[stat.ordinal()]) {
      case 19:
         return this.getAgConsume(value);
      default:
         return this.getMpConsume(value);
      }
   }

   public int getFixedDamage() {
      SkillAdditionalProperty property = this.getProperty();
      return property != null ? property.getValue(StatEnum.DAM_IGNORE, StatModifyPriority.ADD, this.fixedDamage) : this.fixedDamage;
   }

   public int getRate() {
      SkillAdditionalProperty property = this.getProperty();
      return property != null ? property.getValue(StatEnum.PROBABILITY, StatModifyPriority.ADD, this.rate) : this.rate;
   }

   public boolean isCommonSkill() {
      return ProfessionSkills.isCommonSkill(this.getSkillID());
   }

   public int getSkillMoveID() {
      int proType = 10;
      if (this.owner.getType() == 1) {
         proType = ((Player)this.owner).getProType();
      }

      int movementType = this.owner.getMovementType();
      int result = SkillMovement.getSkillMoveID(proType, this.skillID, movementType);
      return result;
   }

   public void destroy() {
      this.owner = null;
      this.isDestroy = true;
   }

   public boolean isDestroy() {
      return this.isDestroy;
   }

   public void learnSkill() {
      if (this.maxPassiveConsume > 0) {
         this.delPassiveConsume(this.maxPassiveConsume);
      }

      this.setLevel(this.level + 1);
   }

   public SkillModel getModel() {
      return SkillModel.getModel(this.skillID);
   }

   public SkillAdditionalProperty getProperty() {
      return this.owner.getSkillManager().getProperty(this.skillID);
   }

   public int getSkillID() {
      return this.skillID;
   }

   public void setSkillID(int skillID) {
      this.skillID = skillID;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public void setCoolTime(int coolTime) {
      this.coolTime = coolTime;
   }

   public void setRange(int range) {
      this.range = range;
   }

   public int getDistance() {
      return this.distance;
   }

   public void setDistance(int distance) {
      this.distance = distance;
   }

   public void setMaxCount(int maxCount) {
      this.maxCount = maxCount;
   }

   public long getNextThawTime() {
      return this.nextThawTime;
   }

   public void setNextThawTime(long nextThawTime) {
      this.nextThawTime = nextThawTime;
   }

   public void setRate(int rate) {
      this.rate = rate;
   }

   public int getQuality() {
      return this.quality;
   }

   public void setQuality(int quality) {
      this.quality = quality;
   }

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }

   public Creature getOwner() {
      return this.owner;
   }

   public boolean isHasCheck() {
      return this.hasCheck;
   }

   public float getSkillCorrection() {
      return this.skillCorrection;
   }

   public void setSkillCorrection(float skillCorrection) {
      this.skillCorrection = skillCorrection;
   }

   public float getSkillFactor() {
      return this.skillFactor;
   }

   public void setSkillFactor(float skillFactor) {
      this.skillFactor = skillFactor;
   }

   public void setFixedDamage(int fixedDamage) {
      this.fixedDamage = fixedDamage;
   }

   public int getPassiveConsume() {
      return this.passiveConsume;
   }

   public void setPassiveConsume(int passiveConsume) {
      this.passiveConsume = passiveConsume;
   }

   public int getMaxPassiveConsume() {
      return this.maxPassiveConsume;
   }

   public int getConsumeValue() {
      return this.consumeValue;
   }

   public int getSkillStep() {
      return this.skillStep;
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$stats$StatEnum() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$stats$StatEnum;
      if ($SWITCH_TABLE$com$mu$game$model$stats$StatEnum != null) {
         return var10000;
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
