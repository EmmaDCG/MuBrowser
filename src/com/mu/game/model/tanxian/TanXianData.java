package com.mu.game.model.tanxian;

import com.mu.game.model.rewardhall.RewardHallConfigManager;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.clazz.TaskClazz;
import com.mu.utils.CommonRegPattern;
import com.mu.utils.Rnd;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import org.jdom.IllegalDataException;

public class TanXianData {
   private int id;
   private String name;
   private int icon;
   private int openLevel;
   private String description;
   private List itemList;
   private int exp;
   private LinkedHashMap taskMap = new LinkedHashMap();
   private int max_weight = 0;

   public TanXianData(int id) {
      this.id = id;
      this.itemList = new ArrayList();
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public int getOpenLevel() {
      return this.openLevel;
   }

   public void setOpenLevel(int openLevel) {
      this.openLevel = openLevel;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public List getItemList() {
      return this.itemList;
   }

   public void setItemListStr(String str) {
      RewardHallConfigManager.parseRewardItemStr(this.itemList, str);
   }

   public int getExp() {
      return this.exp;
   }

   public void setExp(int exp) {
      this.exp = exp;
   }

   public void setTaskStr(String str) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(str);

      while(m.find()) {
         int taskId = Integer.parseInt(m.group());
         TaskData data = TaskConfigManager.getData(taskId);
         if (data == null || !data.is(TaskClazz.TanXian)) {
            throw new IllegalDataException("TanXianData Config error, set task str: " + str);
         }

         if (data.getClazzId() != this.id) {
            throw new IllegalDataException("TanXianData Config error, tanxian mapping task not single");
         }

         m.find();
         int weight = Integer.parseInt(m.group());
         if (weight == 0) {
            throw new IllegalDataException("TanXianData Config error, set task str: weight is 0");
         }

         this.max_weight += weight;
         this.taskMap.put(this.max_weight, data);
      }

      if (this.taskMap.isEmpty()) {
         throw new IllegalDataException("TanXianData Config error, set task str: not have task data");
      }
   }

   public TaskData getData() {
      int rnd = Rnd.get(this.max_weight);
      Iterator it = this.taskMap.keySet().iterator();

      while(it.hasNext()) {
         Integer weight = (Integer)it.next();
         if (rnd < weight.intValue()) {
            return (TaskData)this.taskMap.get(weight);
         }
      }

      return null;
   }
}
