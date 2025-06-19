package com.mu.game.model.task;

public class TaskDialog {
   private int id;
   private String name;
   private String title;
   private int icon;
   private String content;

   public TaskDialog(int id, String name, String title, int icon, String content) {
      this.id = id;
      this.name = name;
      this.title = title;
      this.icon = icon;
      this.content = content;
   }

   public int getId() {
      return this.id;
   }

   public String getName(TaskData data) {
      return this.name != null && !this.name.equals("") ? this.name : data.getName();
   }

   public String getTitle() {
      return this.title;
   }

   public int getIcon() {
      return this.icon;
   }

   public String getContent() {
      return this.content;
   }
}
