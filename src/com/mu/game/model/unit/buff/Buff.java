package com.mu.game.model.unit.buff;

import com.mu.config.Constant;
import com.mu.executor.imp.buff.SaveBuffWhenOffLineExecutor;
import com.mu.game.model.packet.RolePacketService;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.PersistentState;
import com.mu.game.model.stats.StatChange;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.game.model.stats.statId.StatIdCreator;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.model.BuffDynamicData;
import com.mu.game.model.unit.buff.model.BuffGroup;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.buff.model.BuffProps;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.attack.AttackCreature;
import com.mu.io.game.packet.imp.buff.DeleteBuff;
import com.mu.io.game.packet.imp.skill.AddSkill;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Buff {
   private static Logger logger = LoggerFactory.getLogger(Buff.class);
   private Creature owner;
   private int casterType;
   private long casterID;
   private int modelID;
   private int level;
   private long endTime = -1L;
   private long duration;
   private long lastCheckTime;
   private int buffCheckTime;
   private int overlap = 1;
   private boolean isDestory = false;
   private List props = null;
   protected List stats = null;
   private List fartherEffect = null;
   private PersistentState persistentState;
   private boolean hasEffect;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$stats$StatEnum;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$stats$PersistentState;

   public Buff(int modelID, int level, Creature owner, Creature caster) {
      this.persistentState = PersistentState.UPDATED;
      this.hasEffect = true;
      this.persistentState = PersistentState.NEW;
      this.modelID = modelID;
      this.level = level;
      this.owner = owner;
      this.buffCheckTime = 500;
      this.setCaster(caster);
   }

   protected abstract void startDetail();

   protected abstract void doWork(long var1);

   protected abstract void endSpecial(boolean var1);

   public void loadFromDB(long duration, String proStr) {
      this.persistentState = PersistentState.UPDATED;
      List pros = StringTools.analyzeBuffProps(proStr);
      this.initBuff(duration, pros, 2);
   }

   public void loadData(List fartherEffect) {
      this.fartherEffect = fartherEffect;
      this.initBuff(-1L, (List)null, 1);
   }

   protected void initBuff(long duration, List props, int createType) {
      if (createType == 2) {
         this.props = props;
         this.setDuration(duration);
      } else {
         this.addProp();
      }

   }

   protected void addProp() {
      BuffModel model = this.getModel();
      this.setDuration((long)model.getDuration());
      BuffDynamicData dyData = BuffDynamicData.getDyData(this.modelID, this.level);
      if (dyData == null) {
         logger.error("Buff 没有动态数据 ，buffModelID = {},level = {}", this.modelID, this.level);
      } else {
         StatEnum strStat = dyData.getStrStat();
         this.setDuration((long)dyData.getDuration());
         this.props = new ArrayList();

         Iterator var5;
         BuffProps tmpProp;
         label58:
         for(var5 = dyData.getProps().iterator(); var5.hasNext(); this.props.add(tmpProp)) {
            BuffProps prop = (BuffProps)var5.next();
            tmpProp = prop.cloneProp(this.getCaster(), strStat);
            if (this.fartherEffect != null) {
               Iterator fmIt = this.fartherEffect.iterator();

               while(true) {
                  while(true) {
                     FinalModify fm;
                     do {
                        if (!fmIt.hasNext()) {
                           continue label58;
                        }

                        fm = (FinalModify)fmIt.next();
                     } while(tmpProp.getStat() != fm.getStat());

                     fmIt.remove();
                     if (!tmpProp.getStat().isPercent() && tmpProp.getPriority() != fm.getPriority()) {
                        if (tmpProp.getPriority() == StatModifyPriority.ADD) {
                           int addValue = Constant.getPercentValue(tmpProp.getValue(), fm.getValue());
                           tmpProp.addValue(addValue);
                        }
                     } else {
                        tmpProp.addValue(fm.getValue());
                     }
                  }
               }
            }
         }

         if (this.fartherEffect != null && this.fartherEffect.size() > 0) {
            var5 = this.fartherEffect.iterator();

            while(var5.hasNext()) {
               FinalModify fm = (FinalModify)var5.next();
               switch($SWITCH_TABLE$com$mu$game$model$stats$StatEnum()[fm.getStat().ordinal()]) {
               case 106:
                  this.addDuration((long)fm.getValue(), fm.isPercent());
                  break;
               default:
                  tmpProp = new BuffProps(fm.getStat(), fm.getValue(), fm.getValue(), fm.getPriority(), 0);
                  this.props.add(tmpProp);
               }
            }
         }

      }
   }

   protected void addDuration(long addValue, boolean percent) {
      if (this.getDuration() != -1L) {
         if (percent) {
            addValue = (long)((int)(1L * this.getDuration() * addValue / 100000L));
         }

         this.duration += addValue;
         this.duration = Math.max(1L, this.duration);
      }
   }

   protected void resetDuration(long duration) {
      if (this.getDuration() != -1L) {
         if (duration != -1L) {
            this.duration = duration;
         }
      }
   }

   public void check(long now) {
      if (!this.isDestory()) {
         if (this.isHasEffect()) {
            if (now - this.lastCheckTime >= (long)this.getBuffCheckTime()) {
               this.lastCheckTime = now;
               if (this.isEnd(now)) {
                  this.getOwner().getBuffManager().endBuff(this.modelID, true);
               } else {
                  this.doWork(now);
               }
            }
         }
      }
   }

   public final boolean isEnd(long now) {
      if (this.endTime == -1L) {
         return false;
      } else {
         return this.endTime <= now;
      }
   }

   public List getDynamicData() {
      ArrayList datas = new ArrayList();
      double data = 0.0D;
      BigDecimal bg = null;
      BuffDynamicData dyData = BuffDynamicData.getDyData(this.getModelID(), this.getLevel());

      for(Iterator var7 = this.getModel().getDataOrder().iterator(); var7.hasNext(); datas.add(AddSkill.getDyString(data))) {
         StatEnum stat = (StatEnum)var7.next();
         data = 0.0D;
         if (stat == StatEnum.TIME) {
            data = (double)(this.duration / 1000L);
         } else if (stat == StatEnum.PROBABILITY) {
            data = (double)dyData.getRate();
            bg = new BigDecimal(data / 1000.0D);
            data = bg.setScale(1, 4).doubleValue();
         } else {
            if (this.props != null) {
               Iterator var9 = this.props.iterator();

               while(var9.hasNext()) {
                  BuffProps prop = (BuffProps)var9.next();
                  if (prop.getStat() == stat) {
                     data = (double)prop.getValue();
                     if (prop.isPercent()) {
                        bg = new BigDecimal(data / 1000.0D);
                        data = bg.setScale(1, 4).doubleValue();
                     }
                     break;
                  }
               }
            }

            if (data == 0.0D) {
               data = this.getSpecialDyData(stat);
            }
         }

         if (data < 0.0D) {
            data = Math.abs(data);
         }
      }

      return datas;
   }

   protected double getSpecialDyData(StatEnum stat) {
      return 0.0D;
   }

   public void start(boolean load) {
      try {
         this.setEndTime();
         this.setLastCheckTime(System.currentTimeMillis());
         this.checkExclude();
         this.checkEffect();
         if (this.isHasEffect()) {
            this.startDetail();
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   protected void checkEffect() {
      if (this.getModel().getRequirement() != null) {
         this.hasEffect = this.getModel().getRequirement().check(this.getOwner());
      }

   }

   protected void checkExclude() {
      try {
         BuffModel model = this.getModel();
         if (model.isOverlap()) {
            this.doOverlapping();
         }

         this.getOwner().getBuffManager().endBuff(this.getModelID(), true);
         BuffGroup bg = BuffGroup.getBuffGroup(this.getModelID());
         if (bg != null && bg.isMetux()) {
            Iterator var4 = bg.getBuffs().keySet().iterator();

            while(var4.hasNext()) {
               int buffId = ((Integer)var4.next()).intValue();
               this.getOwner().getBuffManager().endBuff(buffId, true);
            }
         }

         bg = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   protected void doOverlapping() {
      BuffModel model = this.getModel();
      switch(model.getOverlapType()) {
      case 1:
         this.doOverlapTime();
         break;
      case 2:
         this.doOverlapStat();
         break;
      case 3:
         this.doOverlapStat();
         this.doOverlapTime();
      }

   }

   private void doOverlapStat() {
      Buff buff = this.getOwner().getBuffManager().getBuff(this.getModelID());
      if (buff != null) {
         List tempprops = buff.getProps();
         int ol = buff.getOverlap();
         if (tempprops != null) {
            if (ol >= buff.getModel().getMaxOverlap()) {
               if (this.props == null) {
                  this.props = new ArrayList();
               }

               this.props.clear();
               this.props.addAll(tempprops);
            } else {
               Iterator var5 = tempprops.iterator();

               while(var5.hasNext()) {
                  BuffProps bp = (BuffProps)var5.next();
                  this.addStatValue(bp.getStat(), bp.getValue());
               }
            }
         }
      }

   }

   protected void doOverlapTime() {
      Buff buff = this.getOwner().getBuffManager().getBuff(this.getModelID());
      if (buff != null && buff.getDuration() != -1L) {
         long remainTime = SaveBuffWhenOffLineExecutor.getRemainTime(buff, System.currentTimeMillis());
         this.setDuration(this.getDuration() + remainTime);
         this.setEndTime();
      }

   }

   private void setEndTime() {
      this.endTime = this.getDuration() == -1L ? -1L : System.currentTimeMillis() + this.getDuration();
   }

   private void addStatValue(StatEnum stat, int value) {
      if (this.getProps() != null) {
         Iterator var4 = this.getProps().iterator();

         while(var4.hasNext()) {
            BuffProps bp = (BuffProps)var4.next();
            if (bp.getStat() == stat) {
               int tempValue = bp.getValue() + value;
               if (tempValue < 0) {
                  tempValue = 0;
               }

               bp.setValue(tempValue);
               break;
            }
         }
      }

   }

   public void end(boolean notice) {
      if (!this.isDestory()) {
         this.isDestory = true;
         this.persistentDoWhenEnd();
         this.removeStatWhenEnd(notice);
         this.getOwner().getBuffManager().removeBuff(this.getModelID());
         this.endSpecial(notice);
         if (notice) {
            this.sendPacketWhenEnd();
         }

         this.destroy();
      }
   }

   protected void persistentDoWhenEnd() {
      this.setPersistent(PersistentState.DELETED);
      if (this.getPersistentState() == PersistentState.DELETED && this.getOwner().getType() == 1) {
         RolePacketService.noticeGatewayDeleteBuff((Player)this.getOwner(), this.getModelID());
      }

   }

   public void setPersistent(PersistentState pState) {
      if (this.persistentState != PersistentState.NOACTION) {
         switch($SWITCH_TABLE$com$mu$game$model$stats$PersistentState()[pState.ordinal()]) {
         case 2:
            if (this.persistentState == PersistentState.NEW) {
               break;
            }
         case 3:
         default:
            if (this.persistentState != PersistentState.NEVER_STORAGE || this.persistentState != PersistentState.NOACTION) {
               this.persistentState = pState;
            }
            break;
         case 4:
            if (this.persistentState == PersistentState.NEW) {
               this.persistentState = PersistentState.NOACTION;
            } else if (this.persistentState != PersistentState.NOACTION) {
               this.persistentState = PersistentState.DELETED;
            }
         }

      }
   }

   protected void removeStatWhenEnd(boolean notice) {
      StatChange.endStat(this.getOwner(), StatIdCreator.createBuffId(this.getModelID()), notice);
   }

   protected void sendPacketWhenEnd() {
      DeleteBuff.sendToClient(this.getOwner(), this.getModelID());
   }

   public boolean isSendStat() {
      if (this.getOwner().isDestroy()) {
         return false;
      } else {
         return this.getOwner().getType() != 1 || !((Player)this.getOwner()).isNew();
      }
   }

   public void destroy() {
      this.owner = null;
      if (this.fartherEffect != null) {
         this.fartherEffect.clear();
         this.fartherEffect = null;
      }

      if (this.props != null) {
         this.props.clear();
         this.props = null;
      }

      if (this.stats != null) {
         this.stats.clear();
         this.stats = null;
      }

   }

   public boolean canOverlap() {
      if (!this.getModel().isOverlap()) {
         return false;
      } else {
         return this.getOverlap() < this.getModel().getMaxOverlap();
      }
   }

   public String getBuffPropStr() {
      return StringTools.getPropStr(this.getProps());
   }

   public int getStatID() {
      return StatIdCreator.createBuffId(this.getModelID());
   }

   public BuffModel getModel() {
      return BuffModel.getModel(this.modelID);
   }

   public Creature getOwner() {
      return this.owner;
   }

   public void setOwner(Creature owner) {
      this.owner = owner;
   }

   public void setCaster(Creature caster) {
      if (caster != null) {
         this.casterType = caster.getType();
         this.casterID = caster.getID();
      }

   }

   public Creature getCaster() {
      return AttackCreature.getAttackTarget(this.getOwner().getMap(), this.casterType, this.casterID);
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public long getEndTime() {
      return this.endTime;
   }

   public long getDuration() {
      return this.duration;
   }

   public void setDuration(long duration) {
      this.duration = duration;
   }

   public long getLastCheckTime() {
      return this.lastCheckTime;
   }

   public void setLastCheckTime(long lastCheckTime) {
      this.lastCheckTime = lastCheckTime;
   }

   public int getBuffCheckTime() {
      return this.buffCheckTime;
   }

   public void setBuffCheckTime(int buffCheckTime) {
      this.buffCheckTime = buffCheckTime;
   }

   public int getOverlap() {
      return this.overlap;
   }

   public void setOverlap(int overlap) {
      this.overlap = Math.min(overlap, this.getModel().getMaxOverlap());
   }

   public List getProps() {
      return this.props;
   }

   public void setProps(List props) {
      this.props = props;
   }

   public List getStats() {
      return this.stats;
   }

   public void setStats(List stats) {
      this.stats = stats;
   }

   public List getFartherEffect() {
      return this.fartherEffect;
   }

   public void setFartherEffect(List fartherEffect) {
      this.fartherEffect = fartherEffect;
   }

   public PersistentState getPersistentState() {
      return this.persistentState;
   }

   public void setPersistentState(PersistentState persistentState) {
      this.persistentState = persistentState;
   }

   public boolean isDestory() {
      return this.isDestory;
   }

   public void setDestory(boolean isDestory) {
      this.isDestory = isDestory;
   }

   public int getCasterType() {
      return this.casterType;
   }

   public void setCasterType(int casterType) {
      this.casterType = casterType;
   }

   public long getCasterID() {
      return this.casterID;
   }

   public void setCasterID(long casterID) {
      this.casterID = casterID;
   }

   public boolean isHasEffect() {
      return this.hasEffect;
   }

   public void setHasEffect(boolean hasEffect) {
      this.hasEffect = hasEffect;
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

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$stats$PersistentState() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$stats$PersistentState;
      if ($SWITCH_TABLE$com$mu$game$model$stats$PersistentState != null) {
         return var10000;
      } else {
         int[] var0 = new int[PersistentState.values().length];

         try {
            var0[PersistentState.DELETED.ordinal()] = 4;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[PersistentState.NEVER_STORAGE.ordinal()] = 6;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[PersistentState.NEW.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[PersistentState.NOACTION.ordinal()] = 5;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[PersistentState.UPDATED.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[PersistentState.UPDATE_REQUIRED.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$stats$PersistentState = var0;
         return var0;
      }
   }
}
