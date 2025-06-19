package com.mu.game.model.unit.material.requirement;

import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.task.Task;
import com.mu.game.model.unit.player.Player;
import java.util.HashSet;

public class TaskRequirement implements MaterialRequirement {
   private static final HashSet taskSet = new HashSet();
   private int taskID;

   public static boolean needCheck(int tid) {
      return taskSet.contains(tid);
   }

   public static void addNeedCheck(int tid) {
      if (!needCheck(tid)) {
         taskSet.add(tid);
      }

   }

   public TaskRequirement(int taskID) {
      this.taskID = taskID;
      addNeedCheck(this.taskID);
   }

   public int match(Player player) {
      PlayerTaskManager tm = player.getTaskManager();
      if (tm == null) {
         return -1;
      } else {
         Task t = (Task)tm.getCurrentTaskMap().get(this.taskID);
         return t != null && !t.isComplete() ? 1 : -1;
      }
   }

   public synchronized void destroy() {
   }

   private String getTaskName() {
      return "";
   }

   public String notMatchMessage(Player player) {
      return this.getTaskName();
   }

   public void endCollect(Player player) {
   }
}
