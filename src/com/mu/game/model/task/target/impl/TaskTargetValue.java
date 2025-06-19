package com.mu.game.model.task.target.impl;

import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.clazz.TaskClazz;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.task.target.TaskTarget;
import com.mu.game.model.task.target.TaskTargetRate;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import java.util.regex.Matcher;

public class TaskTargetValue extends TaskTarget {
   private TargetType.ValueType valueType;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$ValueType;

   public TaskTargetValue(TaskData data, int index, TargetType type, Matcher m) {
      super(data, index, type, m);
   }

   public TargetType.ValueType getValueType() {
      return this.valueType;
   }

   public void parseConfig(Matcher m) {
      m.find();
      this.valueType = TargetType.ValueType.valueOf(Integer.parseInt(m.group()));
      m.find();
      this.maxRate = Integer.parseInt(m.group());
   }

   public void init(Task task) {
      this.accept(task);
   }

   public void accept(Task task) {
      TaskTargetRate rate = task.getRate(this.index);
      if (!task.is(TaskClazz.GH) || !rate.ok()) {
         rate.setRate(this.getRealRate(task.getOwner()));
      }
   }

   public void waive(Task task) {
   }

   public void submit(Task task) {
      switch($SWITCH_TABLE$com$mu$game$model$task$target$TargetType$ValueType()[this.valueType.ordinal()]) {
      case 2:
         if (!task.isForceComplete()) {
            PlayerManager.reduceMoney(task.getOwner(), this.getMaxRate());
         }
      default:
      }
   }

   public void checkRate(Task task, Object... args) {
      try {
         TaskTargetRate rate = task.getRate(this.index);
         if (task.is(TaskClazz.GH) && rate.ok()) {
            return;
         }

         int lastRate = rate.getCurRate();
         int var10000 = $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$ValueType()[this.valueType.ordinal()];
         rate.setRate(this.getRealRate(task.getOwner()));
         if (lastRate != rate.getCurRate()) {
            task.onRateChangeCheckComplete();
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public int getRealRate(Player player) {
      switch($SWITCH_TABLE$com$mu$game$model$task$target$TargetType$ValueType()[this.valueType.ordinal()]) {
      case 1:
         return player.getLevel();
      case 2:
         return player.getMoney();
      case 3:
         return player.getEquipment().getTotalStar();
      case 4:
         return player.getEquipment().getTotalExcellentCount();
      case 5:
      default:
         return super.getRealRate(player);
      case 6:
         return player.getEquipment().getMaxZhuijia();
      case 7:
         return player.getEquipment().getZhuijiaCount();
      case 8:
         return player.getEquipment().getTotalSetStatCount();
      case 9:
         return player.getWarComment();
      case 10:
         return player.getPetManager().getRank().getRank();
      case 11:
         return player.getVIPLevel();
      case 12:
         return player.getGang() == null ? 0 : 1;
      case 13:
         return player.getEquipment().getMaxStarLevel();
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$ValueType() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$ValueType;
      if ($SWITCH_TABLE$com$mu$game$model$task$target$TargetType$ValueType != null) {
         return var10000;
      } else {
         int[] var0 = new int[TargetType.ValueType.values().length];

         try {
            var0[TargetType.ValueType.JiaRuZhanMeng.ordinal()] = 12;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            var0[TargetType.ValueType.MAX_QiangHua.ordinal()] = 13;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            var0[TargetType.ValueType.MAX_TaoZhuang.ordinal()] = 8;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            var0[TargetType.ValueType.MAX_ZhuiJia.ordinal()] = 6;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            var0[TargetType.ValueType.MONEY.ordinal()] = 2;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            var0[TargetType.ValueType.Pet_Rank.ordinal()] = 10;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            var0[TargetType.ValueType.RoleLevel.ordinal()] = 1;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            var0[TargetType.ValueType.SUM_BaoShi.ordinal()] = 5;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[TargetType.ValueType.SUM_QiangHua.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[TargetType.ValueType.SUM_ZhuiJia.ordinal()] = 7;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[TargetType.ValueType.SUM_ZhuoYue.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[TargetType.ValueType.Vip_Level.ordinal()] = 11;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[TargetType.ValueType.ZhanPing.ordinal()] = 9;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$ValueType = var0;
         return var0;
      }
   }
}
