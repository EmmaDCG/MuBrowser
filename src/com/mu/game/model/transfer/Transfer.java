package com.mu.game.model.transfer;

import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.CommonRegPattern;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Transfer {
   private int id;
   private int job1;
   private int job2;
   private String name;
   private int image;
   private int level;
   private List stepList = new ArrayList();
   private List skillList = new ArrayList();
   private TaskData taskHead;
   private List taskList = new ArrayList();
   private boolean aKeyComplete;
   private int aKeyCompleteExpend;
   private Transfer next;

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getJob1() {
      return this.job1;
   }

   public void setJob1(int job1) {
      this.job1 = job1;
   }

   public int getJob2() {
      return this.job2;
   }

   public void setJob2(int job2) {
      this.job2 = job2;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getImage() {
      return this.image;
   }

   public void setImage(int image) {
      this.image = image;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public List getStepList() {
      return this.stepList;
   }

   public List getSkillList() {
      return this.skillList;
   }

   public Transfer getNext() {
      return this.next;
   }

   public void setNext(Transfer next) {
      this.next = next;
   }

   public boolean isAKeyComplete() {
      return this.aKeyComplete;
   }

   public int getAKeyCompleteExpend() {
      return this.aKeyCompleteExpend;
   }

   public void setAKeyCompleteStr(String aKeyCompleteStr) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(aKeyCompleteStr);
      m.find();
      this.aKeyComplete = m.group().equals("1");
      m.find();
      this.aKeyCompleteExpend = Integer.parseInt(m.group());
   }

   public boolean isTaskNotComplete(Player player) {
      PlayerTaskManager ptm = player.getTaskManager();
      Task task = ptm.getCurZZTask();
      return task == null || !task.isComplete();
   }

   public List getTaskList() {
      return this.taskList;
   }

   public TaskData getTaskHead() {
      return this.taskHead;
   }

   public void setTaskHead(TaskData taskHead) {
      this.taskHead = taskHead;
   }
}
