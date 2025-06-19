package com.mu.game.model.unit.material;

public class MaterialGroup {
   private int mapID;
   private int templateID;
   private int x;
   private int y;
   private int num;
   private int radius;
   private int refreshTime;
   private int modelID;
   private String name = "";
   private int collectTime = 2000;
   private boolean isDisappear = false;
   private int[] face;
   private String collectionText = "";

   public int getMapID() {
      return this.mapID;
   }

   public void setMapID(int mapID) {
      this.mapID = mapID;
   }

   public int getTemplateID() {
      return this.templateID;
   }

   public void setTemplateID(int templateID) {
      this.templateID = templateID;
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

   public int getNum() {
      return this.num;
   }

   public void setNum(int num) {
      this.num = num;
   }

   public int getRadius() {
      return this.radius;
   }

   public void setRadius(int radius) {
      this.radius = radius;
   }

   public int getRefreshTime() {
      return this.refreshTime;
   }

   public void setRefreshTime(int refreshTime) {
      this.refreshTime = refreshTime;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getCollectTime() {
      return this.collectTime;
   }

   public void setCollectTime(int collectTime) {
      this.collectTime = collectTime;
   }

   public boolean isDisappear() {
      return this.isDisappear;
   }

   public void setDisappear(boolean isDisappear) {
      this.isDisappear = isDisappear;
   }

   public int[] getFace() {
      return this.face;
   }

   public void setFace(int[] face) {
      this.face = face;
   }

   public String getCollectionText() {
      return this.collectionText;
   }

   public void setCollectionText(String collectionText) {
      this.collectionText = collectionText;
   }
}
