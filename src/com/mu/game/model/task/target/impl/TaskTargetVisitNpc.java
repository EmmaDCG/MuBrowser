package com.mu.game.model.task.target.impl;

import com.mu.game.model.dialog.DialogConfigManager;
import com.mu.game.model.dialog.DialogOptionSee;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.TaskDialog;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.task.target.TaskTarget;
import com.mu.game.model.task.target.TaskTargetRate;
import com.mu.io.game.packet.imp.npc.UpdateNpcHeadIcon;
import java.util.regex.Matcher;

public class TaskTargetVisitNpc extends TaskTarget {
   private long visitNpc;
   private TaskDialog visitDialog;

   public TaskTargetVisitNpc(TaskData data, int index, TargetType type, Matcher m) {
      super(data, index, type, m);
   }

   public void parseConfig(Matcher m) {
      this.maxRate = 1;
      m.find();
      this.visitNpc = Long.parseLong(m.group());
      m.find();
      this.visitDialog = TaskConfigManager.getDialog(Integer.parseInt(m.group()));
      DialogConfigManager.addTaskOption(this.data, this.visitNpc, this.visitDialog, DialogOptionSee.VISIBLE_TASK_VISIT, this);
   }

   public void init(Task task) {
   }

   public void accept(Task task) {
   }

   public void waive(Task task) {
   }

   public void submit(Task task) {
   }

   public void checkRate(Task task, Object... args) {
      try {
         Long npcId = (Long)args[0];
         if (npcId.longValue() != this.visitNpc) {
            return;
         }

         TaskTargetRate rate = task.getRate(this.index);
         if (!rate.ok()) {
            rate.addRate();
            if (rate.ok() && this.data.getSubmitNpc() != this.visitNpc) {
               UpdateNpcHeadIcon.updatePlayerSeeNpcHeadIcon(task.getOwner(), this.visitNpc);
            }

            task.onRateChangeCheckComplete();
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void forceComplete(Task task) {
      UpdateNpcHeadIcon.updatePlayerSeeNpcHeadIcon(task.getOwner(), this.visitNpc);
   }

   public long getVisitNpc() {
      return this.visitNpc;
   }

   public TaskDialog getVisitDialog() {
      return this.visitDialog;
   }
}
