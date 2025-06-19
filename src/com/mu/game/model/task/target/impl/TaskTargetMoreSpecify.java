package com.mu.game.model.task.target.impl;

import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.clazz.TaskClazz;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.task.target.TaskTarget;
import com.mu.game.model.task.target.TaskTargetRate;
import com.mu.game.model.unit.player.Player;
import java.util.regex.Matcher;

public class TaskTargetMoreSpecify extends TaskTarget {
   private TargetType.MoreSpecifyType specifyType;
   private boolean allFill;
   private int[] specifyArr;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$MoreSpecifyType;

   public TaskTargetMoreSpecify(TaskData data, int index, TargetType type, Matcher m) {
      super(data, index, type, m);
   }

   public TargetType.MoreSpecifyType getSpecifyType() {
      return this.specifyType;
   }

   public void parseConfig(Matcher m) {
      m.find();
      this.specifyType = TargetType.MoreSpecifyType.valueOf(Integer.parseInt(m.group()));
      this.maxRate = 1;
      m.find();
      this.allFill = m.group().equals("1");
      m.find();
      this.specifyArr = new int[Integer.parseInt(m.group())];

      for(int i = 0; i < this.specifyArr.length; ++i) {
         m.find();
         this.specifyArr[i] = Integer.parseInt(m.group());
      }

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
   }

   public void checkRate(Task task, Object... args) {
      try {
         TaskTargetRate rate = task.getRate(this.index);
         if (task.is(TaskClazz.GH) && rate.ok()) {
            return;
         }

         int lastRate = rate.getCurRate();
         int var10000 = $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$MoreSpecifyType()[this.specifyType.ordinal()];
         rate.setRate(this.getRealRate(task.getOwner()));
         if (lastRate != rate.getCurRate()) {
            task.onRateChangeCheckComplete();
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public int getRealRate(Player player) {
      int i;
      if (this.allFill) {
         for(i = 0; i < this.specifyArr.length; ++i) {
            if (!this.success(player, this.specifyArr[i], i)) {
               return 0;
            }
         }

         return 1;
      } else {
         for(i = 0; i < this.specifyArr.length; ++i) {
            if (this.success(player, this.specifyArr[i], i)) {
               return 1;
            }
         }

         return 0;
      }
   }

   public boolean success(Player player, int specify, int index) {
      switch($SWITCH_TABLE$com$mu$game$model$task$target$TargetType$MoreSpecifyType()[this.specifyType.ordinal()]) {
      case 1:
         if (player.getEquipment().getItemBySlot(specify) != null) {
            return true;
         }

         return false;
      case 2:
         return player.getEquipment().hasItemByModel(specify);
      case 3:
         if (player.getSkillManager().getSkillLevel(specify) > 0) {
            return true;
         }

         return false;
      case 4:
         if (player.getProType() == index && player.getBackpack().getItemCount(specify) > 0) {
            return true;
         }

         return false;
      default:
         return false;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$MoreSpecifyType() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$MoreSpecifyType;
      if ($SWITCH_TABLE$com$mu$game$model$task$target$TargetType$MoreSpecifyType != null) {
         return var10000;
      } else {
         int[] var0 = new int[TargetType.MoreSpecifyType.values().length];

         try {
            var0[TargetType.MoreSpecifyType.Equiment_Item.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[TargetType.MoreSpecifyType.Equiment_Position.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[TargetType.MoreSpecifyType.ITEM.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[TargetType.MoreSpecifyType.SKILL.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$MoreSpecifyType = var0;
         return var0;
      }
   }
}
