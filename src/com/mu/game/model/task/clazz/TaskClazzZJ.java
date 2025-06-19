package com.mu.game.model.task.clazz;

import com.mu.game.model.task.TaskData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskClazzZJ {
   private int id;
   private int index;
   private String name;
   private List taskList;

   public TaskClazzZJ(int id, String name) {
      this.id = id;
      this.name = name;
      this.taskList = new ArrayList();
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public int getSize() {
      return this.taskList.size();
   }

   public Iterator getIterator() {
      return this.taskList.iterator();
   }

   public void addTask(TaskData data) {
      this.taskList.add(data);
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }
}
