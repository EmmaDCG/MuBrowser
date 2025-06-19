package com.mu.game.model.task.target.impl;

import com.mu.game.model.guide.TaskActionManager;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.task.target.TaskTarget;
import com.mu.game.model.task.target.TaskTargetRate;
import com.mu.game.model.unit.player.Player;
import java.util.regex.Matcher;

public class TaskTargetSpecify extends TaskTarget {
   private TargetType.SpecifyType specifyType;
   private int specifyId;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$SpecifyType;

   public TaskTargetSpecify(TaskData data, int index, TargetType type, Matcher m) {
      super(data, index, type, m);
   }

   public TargetType.SpecifyType getSpecifyType() {
      return this.specifyType;
   }

   public void parseConfig(Matcher m) {
      m.find();
      this.specifyType = TargetType.SpecifyType.valueOf(Integer.parseInt(m.group()));
      m.find();
      this.specifyId = Integer.parseInt(m.group());
      m.find();
      this.maxRate = Integer.parseInt(m.group());
   }

   public void init(Task task) {
      this.accept(task);
   }

   public void accept(Task task) {
      switch($SWITCH_TABLE$com$mu$game$model$task$target$TargetType$SpecifyType()[this.specifyType.ordinal()]) {
      case 3:
      case 7:
      case 8:
      case 10:
      case 11:
         TaskTargetRate rate = task.getRate(this.index);
         rate.setRate(this.getRealRate(task.getOwner()));
      case 4:
      case 5:
      case 6:
      case 9:
      default:
      }
   }

   public void waive(Task task) {
   }

   public void submit(Task task) {
      switch($SWITCH_TABLE$com$mu$game$model$task$target$TargetType$SpecifyType()[this.specifyType.ordinal()]) {
      case 3:
         if (!task.isForceComplete()) {
            task.getOwner().getItemManager().deleteItemByModel(this.specifyId, this.maxRate, 5);
         }
      default:
      }
   }

   public void checkRate(Task task, Object... args) {
      try {
         TaskTargetRate rate = task.getRate(this.index);
         int r;
         switch($SWITCH_TABLE$com$mu$game$model$task$target$TargetType$SpecifyType()[this.specifyType.ordinal()]) {
         case 3:
            r = ((Integer)args[0]).intValue();
            if (this.specifyId != r) {
               return;
            }

            int r2 = rate.getCurRate();
            rate.setRate(this.getRealRate(task.getOwner()));
            if (r2 != rate.getCurRate()) {
               if (r2 > rate.getCurRate()) {
                  TaskActionManager.doRateRecude(task.getOwner(), task.getId());
               }

               task.onRateChangeCheckComplete();
            }
            break;
         case 4:
         case 5:
         case 6:
         case 9:
         default:
            if (rate.ok()) {
               return;
            }

            r = ((Integer)args[0]).intValue();
            if (this.specifyId != r) {
               return;
            }

            if (this.specifyType == TargetType.SpecifyType.FB_Appraise) {
               rate.setRate(((Integer)args[1]).intValue());
            } else {
               rate.addRate();
            }

            task.onRateChangeCheckComplete();
            break;
         case 7:
         case 8:
         case 10:
         case 11:
            r = rate.getCurRate();
            rate.setRate(this.getRealRate(task.getOwner()));
            if (r != rate.getCurRate()) {
               if (r > rate.getCurRate()) {
                  TaskActionManager.doRateRecude(task.getOwner(), task.getId());
               }

               task.onRateChangeCheckComplete();
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public int getRealRate(Player player) {
      switch($SWITCH_TABLE$com$mu$game$model$task$target$TargetType$SpecifyType()[this.specifyType.ordinal()]) {
      case 3:
         return player.getBackpack().getItemCount(this.specifyId);
      case 4:
      case 5:
      case 6:
      case 9:
      default:
         return super.getRealRate(player);
      case 7:
         return player.getEquipment().getItemCountByLevel(this.specifyId);
      case 8:
         return player.getEquipment().hasASetByLevelAndStar(this.specifyId, this.maxRate) ? this.maxRate : 0;
      case 10:
         return player.getEquipment().getExcellCountByLevel(this.specifyId);
      case 11:
         return player.getEquipment().getLuckyCountByLevel(this.specifyId);
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$SpecifyType() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$SpecifyType;
      if ($SWITCH_TABLE$com$mu$game$model$task$target$TargetType$SpecifyType != null) {
         return var10000;
      } else {
         int[] var0 = new int[TargetType.SpecifyType.values().length];

         try {
            var0[TargetType.SpecifyType.COUNT_XinYun.ordinal()] = 11;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            var0[TargetType.SpecifyType.COUNT_ZhuoYue.ordinal()] = 10;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            var0[TargetType.SpecifyType.CollectionItem.ordinal()] = 3;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            var0[TargetType.SpecifyType.FB_Appraise.ordinal()] = 6;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            var0[TargetType.SpecifyType.FB_Enter_Count.ordinal()] = 5;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            var0[TargetType.SpecifyType.HC_COUNT.ordinal()] = 9;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[TargetType.SpecifyType.KillMonster.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[TargetType.SpecifyType.SubmitTask.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[TargetType.SpecifyType.UseItem.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[TargetType.SpecifyType.ZB_Jie_Count.ordinal()] = 7;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[TargetType.SpecifyType.ZB_Jie_Jia_Count.ordinal()] = 8;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$task$target$TargetType$SpecifyType = var0;
         return var0;
      }
   }
}
