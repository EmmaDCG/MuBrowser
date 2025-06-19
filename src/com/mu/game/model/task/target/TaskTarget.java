package com.mu.game.model.task.target;

import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.target.impl.TaskTargetCount;
import com.mu.game.model.task.target.impl.TaskTargetMoreSpecify;
import com.mu.game.model.task.target.impl.TaskTargetSpecify;
import com.mu.game.model.task.target.impl.TaskTargetValue;
import com.mu.game.model.task.target.impl.TaskTargetVisitNpc;
import com.mu.game.model.unit.player.Player;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TaskTarget {
   private static Logger logger = LoggerFactory.getLogger(TaskTarget.class);
   protected int index;
   protected TargetType type;
   protected int maxRate;
   protected TaskData data;
   protected boolean destroyed;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$task$target$TargetType;

   public TaskTarget(TaskData data, int index, TargetType type, Matcher m) {
      this.data = data;
      this.index = index;
      this.type = type;
      this.parseConfig(m);
   }

   public static TaskTarget newInstance(TaskData data, int index, int typeId, Matcher m) {
      try {
         TargetType type = TargetType.valueOf(typeId);
         TaskTarget target = null;
         switch($SWITCH_TABLE$com$mu$game$model$task$target$TargetType()[type.ordinal()]) {
         case 1:
            target = new TaskTargetCount(data, index, type, m);
            break;
         case 2:
            target = new TaskTargetSpecify(data, index, type, m);
            break;
         case 3:
            target = new TaskTargetValue(data, index, type, m);
            break;
         case 4:
            target = new TaskTargetVisitNpc(data, index, type, m);
            break;
         case 5:
            target = new TaskTargetMoreSpecify(data, index, type, m);
            break;
         default:
            logger.error("Task[{}] config error, not fount target type {}", data.getId(), typeId);
         }

         return (TaskTarget)target;
      } catch (Exception var6) {
         var6.printStackTrace();
         return null;
      }
   }

   public abstract void parseConfig(Matcher var1);

   public void login(Task task) {
   }

   public abstract void init(Task var1);

   public abstract void accept(Task var1);

   public abstract void waive(Task var1);

   public abstract void submit(Task var1);

   public abstract void checkRate(Task var1, Object... var2);

   public void forceComplete(Task task) {
   }

   public int getRealRate(Player player) {
      return 0;
   }

   public int getIndex() {
      return this.index;
   }

   public TargetType getType() {
      return this.type;
   }

   public boolean is(TargetType type) {
      return this.getType() == type;
   }

   public int getMaxRate() {
      return this.maxRate;
   }

   public TaskData getData() {
      return this.data;
   }

   public boolean isDestroyed() {
      return this.destroyed;
   }

   public void destroy(Task task) {
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$task$target$TargetType() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$task$target$TargetType;
      if ($SWITCH_TABLE$com$mu$game$model$task$target$TargetType != null) {
         return var10000;
      } else {
         int[] var0 = new int[TargetType.values().length];

         try {
            var0[TargetType.COUNT.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[TargetType.MoreSPECIFY.ordinal()] = 5;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[TargetType.SPECIFY.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[TargetType.VALUE.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[TargetType.VisitNpc.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$task$target$TargetType = var0;
         return var0;
      }
   }
}
