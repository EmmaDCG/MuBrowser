package com.mu.game.model.task.clazz;

import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.task.TaskData;
import com.mu.utils.CommonRegPattern;
import com.mu.utils.Rnd;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class TaskClazzRC {
   private int level;
   private double[] rewardStarFactors;
   private int refreshStarExpend;
   private String refreshStarStr;
   private List taskSet;
   private List baseRewardList;
   private int submitFiveMultipleExpend = 0;
   private int buyCountExpend;

   public TaskClazzRC(int level) {
      this.level = level;
   }

   public int getRefreshStarExpend() {
      return this.refreshStarExpend;
   }

   public void setRefreshStarExpend(int refreshStarExpend) {
      this.refreshStarExpend = refreshStarExpend;
   }

   public List getTaskSet() {
      return this.taskSet;
   }

   public void setTaskSetStr(String taskSetStr) {
      this.taskSet = new ArrayList();
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(taskSetStr);

      while(m.find()) {
         int taskId = Integer.parseInt(m.group());
         TaskData data = TaskConfigManager.getData(taskId);
         if (data != null && !data.isDisabled()) {
            this.taskSet.add(data);
         }
      }

   }

   public List getBaseRewardList() {
      return this.baseRewardList;
   }

   public void setBaseRewardStr(String baseRewardStr) {
      this.baseRewardList = new ArrayList();
      TaskConfigManager.parseRewardItemStr(this.baseRewardList, baseRewardStr);
   }

   public int getLevel() {
      return this.level;
   }

   public TaskData randomTask() {
      return this.taskSet.isEmpty() ? null : (TaskData)this.taskSet.get(Rnd.get(this.taskSet.size()));
   }

   public double getRewardStarFactor(int star) {
      return this.rewardStarFactors[Math.max(1, Math.min(10, star)) - 1];
   }

   public void setRewardStarFactorStr(String rewardStarFactorStr) {
      this.rewardStarFactors = new double[10];
      String[] dataStr = rewardStarFactorStr.split(",");

      for(int i = 0; i < this.rewardStarFactors.length; ++i) {
         this.rewardStarFactors[i] = Double.parseDouble(dataStr[i]);
      }

   }

   public String getRefreshStarStr() {
      return this.refreshStarStr;
   }

   public void setRefreshStarStr(String refreshStarStr) {
      this.refreshStarStr = refreshStarStr;
   }

   public int getSubmitFiveMultipleExpend() {
      return this.submitFiveMultipleExpend;
   }

   public void setSubmitFiveMultipleExpend(int submitFiveMultipleExpend) {
      this.submitFiveMultipleExpend = submitFiveMultipleExpend;
   }

   public int getBuyCountExpend() {
      return this.buyCountExpend;
   }

   public void setBuyCountExpend(int buyCountExpend) {
      this.buyCountExpend = buyCountExpend;
   }
}
