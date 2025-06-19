package com.mu.game.model.unit.trigger.action;

import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.trigger.action.imp.AttackBossStopBuff;
import com.mu.game.model.unit.trigger.action.imp.AttackRecoveryHpAction;
import com.mu.game.model.unit.trigger.action.imp.AttackTargetDebuffAction;
import com.mu.game.model.unit.trigger.action.imp.AttackTargetReduceHpAction;
import com.mu.game.model.unit.trigger.action.imp.BeAttackedFanzhenAction;
import com.mu.game.model.unit.trigger.action.imp.KillMonsterRecoverAgAction;
import com.mu.game.model.unit.trigger.action.imp.KillMonsterRecoverHpAction;
import com.mu.game.model.unit.trigger.action.imp.KillMonsterRecoverMpAction;
import com.mu.game.model.unit.trigger.action.imp.MoveStopBuffAction;
import com.mu.game.model.unit.trigger.action.imp.PKStopBuffAction;
import com.mu.game.model.unit.trigger.action.imp.UseSkillStopBuffAction;
import com.mu.game.model.unit.trigger.action.model.TriggerModel;
import com.mu.game.model.unit.trigger.action.model.TriggerStats;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class TriggerActionFactory {
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$unit$trigger$action$ActionEnum;

   public static TriggerAction createAction(int id, Object... objects) {
      try {
         TriggerModel model = TriggerModel.getModel(id);
         if (model == null) {
            return null;
         }

         ActionEnum ae = ActionEnum.find(model.getTriggereID());
         switch($SWITCH_TABLE$com$mu$game$model$unit$trigger$action$ActionEnum()[ae.ordinal()]) {
         case 2:
            return createKillMonsterRecoverHp(id, objects);
         case 3:
            return createKillMonsterRecoverMp(id, objects);
         case 4:
            return createKillMonsterRecoverAg(id, objects);
         case 5:
            return createAttackRecoveryHp(id);
         case 6:
            return createAttackTragetDebuff(id);
         case 7:
            return createAttackTargetReduceHp(id);
         case 8:
         default:
            break;
         case 9:
            return createMoveStopBuff(id);
         case 10:
            return createUseSkillStopBuff(id);
         case 11:
            return createPKStopBuff(id);
         case 12:
            return createAttackBossStopBuff(id);
         case 13:
            return createFanzhen(id, objects);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return null;
   }

   private static KillMonsterRecoverHpAction createKillMonsterRecoverHp(int id, Object... objects) throws Exception {
      int v = ((Integer)objects[0]).intValue();
      if (v < 1) {
         throw new Exception("触发-配置ID = " + id);
      } else {
         return new KillMonsterRecoverHpAction(id, v);
      }
   }

   private static KillMonsterRecoverMpAction createKillMonsterRecoverMp(int id, Object... objects) throws Exception {
      int v = ((Integer)objects[0]).intValue();
      if (v < 1) {
         throw new Exception("触发-配置ID = " + id);
      } else {
         return new KillMonsterRecoverMpAction(id, v);
      }
   }

   private static KillMonsterRecoverAgAction createKillMonsterRecoverAg(int id, Object... objects) throws Exception {
      int v = ((Integer)objects[0]).intValue();
      if (v < 1) {
         throw new Exception("触发-配置ID = " + id);
      } else {
         return new KillMonsterRecoverAgAction(id, v);
      }
   }

   private static BeAttackedFanzhenAction createFanzhen(int id, Object... objects) throws Exception {
      int v = ((Integer)objects[0]).intValue();
      if (v < 1) {
         throw new Exception("触发-配置ID");
      } else {
         return new BeAttackedFanzhenAction(id, v);
      }
   }

   private static AttackTargetDebuffAction createAttackTragetDebuff(int id) throws Exception {
      String[] splits = TriggerModel.getModel(id).getOtherParams().split(",");
      int buffID = Integer.parseInt(splits[0]);
      int level = Integer.parseInt(splits[1]);
      StatEnum triggerStat = StatEnum.find(Integer.parseInt(splits[2]));
      StatEnum resStat = StatEnum.find(Integer.parseInt(splits[3]));
      return new AttackTargetDebuffAction(id, buffID, level, triggerStat, resStat);
   }

   private static AttackRecoveryHpAction createAttackRecoveryHp(int id) throws Exception {
      String[] splits = TriggerModel.getModel(id).getOtherParams().split(",");
      int value = Integer.parseInt(splits[0]);
      AttackRecoveryHpAction aa = new AttackRecoveryHpAction(id, value);
      return aa;
   }

   private static AttackTargetReduceHpAction createAttackTargetReduceHp(int id) throws Exception {
      String[] splits = TriggerModel.getModel(id).getOtherParams().split(",");
      int value = Integer.parseInt(splits[0]);
      StatEnum triggerStat = StatEnum.find(Integer.parseInt(splits[1]));
      StatEnum resStat = StatEnum.find(Integer.parseInt(splits[2]));
      AttackTargetReduceHpAction aa = new AttackTargetReduceHpAction(id, value, triggerStat, resStat);
      return aa;
   }

   private static MoveStopBuffAction createMoveStopBuff(int id) throws Exception {
      String[] splits = TriggerModel.getModel(id).getOtherParams().split(",");
      int buffID = Integer.parseInt(splits[0]);
      return new MoveStopBuffAction(id, buffID);
   }

   private static UseSkillStopBuffAction createUseSkillStopBuff(int id) throws Exception {
      String[] splits = TriggerModel.getModel(id).getOtherParams().split(",");
      int buffID = Integer.parseInt(splits[0]);
      return new UseSkillStopBuffAction(id, buffID);
   }

   private static PKStopBuffAction createPKStopBuff(int id) throws Exception {
      String[] splits = TriggerModel.getModel(id).getOtherParams().split(",");
      int buffID = Integer.parseInt(splits[0]);
      return new PKStopBuffAction(id, buffID);
   }

   private static PKStopBuffAction createAttackBossStopBuff(int id) throws Exception {
      String[] splits = TriggerModel.getModel(id).getOtherParams().split(",");
      int buffID = Integer.parseInt(splits[0]);
      return new AttackBossStopBuff(id, buffID);
   }

   public static List getTriggerAtions(List modifies) {
      List actions = null;
      HashMap stats = null;
      Iterator var4 = modifies.iterator();

      TriggerStats ts;
      TriggerAction action;
      while(var4.hasNext()) {
         FinalModify modify = (FinalModify)var4.next();
         ts = TriggerStats.getTriggerStat(modify.getStat());
         if (ts != null) {
            if (ts.isSpecial()) {
               if (stats == null) {
                  stats = new HashMap();
               }

               int value = modify.getValue();
               if (stats.containsKey(modify.getStat())) {
                  value += ((Integer)stats.get(ts.getStat())).intValue();
               }

               stats.put(ts.getStat(), value);
            } else {
               action = createAction(modify.getValue());
               if (action != null) {
                  if (actions == null) {
                     actions = new ArrayList();
                  }

                  actions.add(action);
               }
            }
         }
      }

      if (stats != null) {
         Iterator it = stats.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            ts = TriggerStats.getTriggerStat((StatEnum)entry.getKey());
            action = createAction(ts.getModelID(), entry.getValue());
            if (action != null) {
               if (actions == null) {
                  actions = new ArrayList();
               }

               actions.add(action);
            }
         }

         stats.clear();
         stats = null;
      }

      return actions;
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$unit$trigger$action$ActionEnum() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$unit$trigger$action$ActionEnum;
      if ($SWITCH_TABLE$com$mu$game$model$unit$trigger$action$ActionEnum != null) {
         return var10000;
      } else {
         int[] var0 = new int[ActionEnum.values().length];

         try {
            var0[ActionEnum.Attack_BossEndBuff.ordinal()] = 12;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            var0[ActionEnum.Attack_EndBuff.ordinal()] = 8;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            var0[ActionEnum.Attack_Recover_Hp.ordinal()] = 5;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            var0[ActionEnum.Attack_Target_DeBuff.ordinal()] = 6;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            var0[ActionEnum.Attack_Traget_ReduceHP.ordinal()] = 7;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            var0[ActionEnum.BeAttack_Fanzhen.ordinal()] = 13;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            var0[ActionEnum.Kill_Monster_Recover_AG.ordinal()] = 4;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            var0[ActionEnum.Kill_Monster_Recover_HP.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[ActionEnum.Kill_Monster_Recover_Mp.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[ActionEnum.Move_EndBuff.ordinal()] = 9;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[ActionEnum.None.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[ActionEnum.PK_EndBuff.ordinal()] = 11;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[ActionEnum.UseSkill_EndBuff.ordinal()] = 10;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$unit$trigger$action$ActionEnum = var0;
         return var0;
      }
   }
}
