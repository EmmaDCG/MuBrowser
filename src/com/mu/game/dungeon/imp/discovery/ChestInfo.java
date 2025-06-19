package com.mu.game.dungeon.imp.discovery;

public class ChestInfo {
   private int templateId;
   private String name;
   private int x;
   private int y;
   private int modelId;

   public int getTemplateId() {
      return this.templateId;
   }

   public void setTemplateId(int templateId) {
      this.templateId = templateId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
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

   public int getModelId() {
      return this.modelId;
   }

   public void setModelId(int modelId) {
      this.modelId = modelId;
   }
}
