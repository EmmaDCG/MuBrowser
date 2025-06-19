package com.mu.game.model.map;

public class NpcInfo {
   private int id;
   private int templateId;
   private int x;
   private int y;
   private String name;
   private int[] face;
   private long dialogId;

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getTemplateId() {
      return this.templateId;
   }

   public void setTemplateId(int tid) {
      this.templateId = tid;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int[] getFace() {
      return this.face;
   }

   public void setFace(int[] face) {
      this.face = face;
   }

   public long getDialogId() {
      return this.dialogId;
   }

   public void setDialogId(long dialogId) {
      this.dialogId = dialogId;
   }
}
