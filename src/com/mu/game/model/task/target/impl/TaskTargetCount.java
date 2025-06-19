package com.mu.game.model.task.target.impl;

import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.task.target.TaskTarget;
import com.mu.game.model.task.target.TaskTargetRate;
import java.util.regex.Matcher;

public class TaskTargetCount extends TaskTarget {
   private TargetType.CountType countType;

   public TaskTargetCount(TaskData data, int index, TargetType type, Matcher m) {
      super(data, index, type, m);
   }

   public TargetType.CountType getCountType() {
      return this.countType;
   }

   public void parseConfig(Matcher m) {
      m.find();
      this.countType = TargetType.CountType.valueOf(Integer.parseInt(m.group()));
      m.find();
      this.maxRate = Integer.parseInt(m.group());
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
         TaskTargetRate rate = task.getRate(this.index);
         if (!rate.ok()) {
            rate.addRate();
            task.onRateChangeCheckComplete();
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
