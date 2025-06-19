package com.mu.game.model.task.clazz;

import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.TaskPosition;
import com.mu.game.model.task.TaskRewardItemData;
import com.mu.utils.CommonRegPattern;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

public class TaskClazzXS {
   private int id;
   private int sort;
   private int level;
   private int color;
   private String name;
   private int icon;
   private int positionId;
   private int positionIcon;
   private String description;
   private TaskRewardItemData openItem;
   private List showRewardList;
   private List taskList;
   private int oneDayCountLimit;
   private TaskData taskHeader;
   private int hcPanelId1;
   private int hcPanelId2;
   private int hcPanelId3;
   private List continueDisabledList = new ArrayList();

   public TaskClazzXS(int id) {
      this.id = id;
      this.taskList = new ArrayList();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public TaskRewardItemData getOpenItem() {
      return this.openItem;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getColor() {
      return this.color;
   }

   public void setColor(int color) {
      this.color = color;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public int getId() {
      return this.id;
   }

   public List getShowRewardList() {
      return this.showRewardList;
   }

   public int getSize() {
      return this.taskList.size();
   }

   public Iterator getIterator() {
      return this.taskList.iterator();
   }

   public void addTask(TaskData data) {
      if (data.isDisabled()) {
         this.continueDisabledList.add(data);
         data.setClazzIndex(this.getSize());
      } else {
         if (this.taskHeader == null) {
            this.taskHeader = data;
         } else {
            ((TaskData)this.taskList.get(this.taskList.size() - 1)).setClazzNext(data);
         }

         Iterator it = this.continueDisabledList.iterator();

         while(it.hasNext()) {
            ((TaskData)it.next()).setClazzNext(data);
         }

         this.continueDisabledList.clear();
         data.setClazzIndex(this.getSize());
         this.taskList.add(data);
      }

   }

   public void setShowRewardList(String rewardStr) {
      this.showRewardList = new ArrayList();
      TaskConfigManager.parseRewardItemStr(this.showRewardList, rewardStr);
   }

   public TaskData getTaskHeader() {
      return this.taskHeader;
   }

   public List getTaskList() {
      return this.taskList;
   }

   public int getSort() {
      return this.sort;
   }

   public void setSort(int sort) {
      this.sort = sort;
   }

   public int getMapId() {
      TaskPosition position = TaskConfigManager.getPosition(this.positionId);
      return position == null ? 0 : position.getMapId();
   }

   public int getPositionId() {
      return this.positionId;
   }

   public void setPositionId(int positionId) {
      this.positionId = positionId;
   }

   public int getPositionIcon() {
      return this.positionIcon;
   }

   public void setPositionIcon(int positionIcon) {
      this.positionIcon = positionIcon;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setOpenItem(String str) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(str);
      m.find();
      int id = Integer.parseInt(m.group());
      m.find();
      int count = Integer.parseInt(m.group());
      this.openItem = TaskRewardItemData.newInstance(id, count, false, 0, -1L);
   }

   public int getOneDayCountLimit() {
      return this.oneDayCountLimit;
   }

   public void setOneDayCountLimit(int oneDayCountLimit) {
      this.oneDayCountLimit = oneDayCountLimit;
   }

   public void setHCPanelStr(String hcStr) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(hcStr);
      m.find();
      this.hcPanelId1 = Integer.parseInt(m.group());
      m.find();
      this.hcPanelId2 = Integer.parseInt(m.group());
      m.find();
      this.hcPanelId3 = Integer.parseInt(m.group());
   }

   public int getHcPanelId1() {
      return this.hcPanelId1;
   }

   public int getHcPanelId2() {
      return this.hcPanelId2;
   }

   public int getHcPanelId3() {
      return this.hcPanelId3;
   }
}
