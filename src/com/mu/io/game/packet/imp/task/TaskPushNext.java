package com.mu.io.game.packet.imp.task;

import com.mu.config.Global;
import com.mu.game.model.guide.TaskActionManager;
import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.util.concurrent.ScheduledFuture;

public class TaskPushNext extends WriteOnlyPacket {
   public TaskPushNext() {
      super(40024);
   }

   public static void pushTaskContinue(final Player player) {
      ScheduledFuture future = player.getPushTaskFuture();
      if (future == null || future.isCancelled() || future.isDone()) {
         player.setPushTaskFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
            public void run() {
               try {
                  TaskPushNext pn = new TaskPushNext();
                  pn.writeInt(player.getTaskManager().getCurZJTask().getId());
                  player.writePacket(pn);
                  pn.destroy();
                  pn = null;
               } catch (Exception var2) {
                  var2.printStackTrace();
               }

            }
         }, 1000L));
      }
   }

   public static boolean needDoPush(Player player) {
      if (!Global.isAutoTask()) {
         return false;
      } else {
         PlayerTaskManager tm = player.getTaskManager();
         if (tm != null) {
            Task task = tm.getCurZJTask();
            if (task == null) {
               return false;
            } else if (task.getData().getClazzIndex() >= TaskConfigManager.TASK_TRACE_ZJ_INDEX) {
               return false;
            } else {
               return !TaskActionManager.dontAuto(task.getId());
            }
         } else {
            return false;
         }
      }
   }

   public static void directPushNext(Player player) {
      try {
         Task task = player.getTaskManager().getCurZJTask();
         if (task != null) {
            pushTaskContinue(player);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static boolean pushNext(Player player, boolean force) {
      if (!Global.isAutoTask()) {
         return false;
      } else {
         PlayerTaskManager tm = player.getTaskManager();
         long loginTime = player.getLoginTime();
         long now = System.currentTimeMillis();
         if ((loginTime <= 0L || now - loginTime >= 5000L) && player.isEnterMap()) {
            if (tm != null) {
               Task task = tm.getCurZJTask();
               if (task == null) {
                  return false;
               }

               if (task.getData().getClazzIndex() >= TaskConfigManager.TASK_TRACE_ZJ_INDEX) {
                  return false;
               }

               if (TaskActionManager.dontAuto(task.getId())) {
                  return true;
               }

               try {
                  pushTaskContinue(player);
                  return true;
               } catch (Exception var7) {
                  var7.printStackTrace();
               }
            }

            return false;
         } else {
            return true;
         }
      }
   }
}
