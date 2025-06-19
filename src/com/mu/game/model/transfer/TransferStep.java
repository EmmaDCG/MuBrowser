package com.mu.game.model.transfer;

import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.CommonRegPattern;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferStep {
   private static Logger logger = LoggerFactory.getLogger(TransferStep.class);
   private int transfer;
   private int job;
   private int step;
   private String description;
   private int attributeDot;
   private List attributeList = new ArrayList();
   private LinkedList taskList = new LinkedList();
   private TransferStep next;

   public int getTransfer() {
      return this.transfer;
   }

   public void setTransfer(int transfer) {
      this.transfer = transfer;
   }

   public int getJob() {
      return this.job;
   }

   public void setJob(int job) {
      this.job = job;
   }

   public int getStep() {
      return this.step;
   }

   public void setStep(int step) {
      this.step = step;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public List getTaskList() {
      return this.taskList;
   }

   public void setTaskStr(String taskStr) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(taskStr);

      while(m.find()) {
         int taskId = Integer.parseInt(m.group());
         TaskData data = TaskConfigManager.getData(taskId);
         if (data == null) {
            logger.error("TransferStep config error, not found task by id {}", taskId);
         } else {
            this.taskList.add(data);
         }
      }

   }

   public TransferStep getNext() {
      return this.next;
   }

   public void setNext(TransferStep next) {
      this.next = next;
   }

   public int getAttributeDot() {
      return this.attributeDot;
   }

   public void setAttributeDot(int attributeDot) {
      this.attributeDot = attributeDot;
   }

   public List getAttributeList() {
      return this.attributeList;
   }

   public void setAttributeListStr(String attributeListStr) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(attributeListStr);

      while(m.find()) {
         StatEnum stat = StatEnum.find(Integer.parseInt(m.group()));
         m.find();
         int value = Integer.parseInt(m.group());
         m.find();
         int addType = Integer.parseInt(m.group());
         FinalModify fm = new FinalModify(stat, value, StatModifyPriority.fine(addType));
         this.attributeList.add(fm);
      }

   }

   public void complete(Task task) {
      if (!this.taskList.isEmpty() && task.getData() == this.taskList.getLast()) {
         Player player = task.getOwner();
         player.addPotential(this.getAttributeDot(), true, true);
      }

   }
}
