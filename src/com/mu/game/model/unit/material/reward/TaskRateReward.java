package com.mu.game.model.unit.material.reward;

import com.mu.game.model.task.Task;
import com.mu.game.model.unit.player.Player;

public class TaskRateReward implements MaterialReward {
   private int taskId;

   public TaskRateReward(int taskId) {
      this.taskId = taskId;
   }

   public int doReword(Player player) {
      return 0;
   }

   public void destroy() {
   }

   public int canReward(Player player) {
      Task task = (Task)player.getTaskManager().getCurrentTaskMap().get(this.taskId);
      if (task != null) {
         task.isComplete();
      }

      return 1;
   }
}
