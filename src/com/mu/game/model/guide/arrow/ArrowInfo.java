package com.mu.game.model.guide.arrow;

import com.mu.utils.Tools;

public class ArrowInfo {
   private int id;
   private String content;
   private boolean inDb = false;
   private int times = 0;
   private boolean saveMore = false;
   private String other = null;
   private int taskId = 0;

   public ArrowInfo(int id) {
      this.id = id;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public boolean isInDb() {
      return this.inDb;
   }

   public void setInDb(boolean inDb) {
      this.inDb = inDb;
   }

   public int getTimes() {
      return this.times;
   }

   public void setTimes(int times) {
      this.times = times;
   }

   public int getId() {
      return this.id;
   }

   public boolean isSaveMore() {
      return this.saveMore;
   }

   public void setSaveMore(boolean saveMore) {
      this.saveMore = saveMore;
   }

   public String getOther() {
      return this.other;
   }

   public void setOther(String other) {
      this.other = other;
   }

   public int getTaskId() {
      if (this.taskId == 0 && this.other != null && Tools.isNumber(this.other)) {
         this.taskId = Integer.parseInt(this.other);
      }

      return this.taskId;
   }
}
