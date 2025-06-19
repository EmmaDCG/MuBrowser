package com.mu.game.model.map;

public class TranspointData {
   long id;
   String name;
   int mapID;
   int x;
   int y;
   int targetMapID;
   int targetX;
   int targetY;
   int worldX;
   int worldY;
   String reqStr = null;

   public TranspointData(long id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getMapID() {
      return this.mapID;
   }

   public void setMapID(int mapID) {
      this.mapID = mapID;
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

   public int getTargetMapID() {
      return this.targetMapID;
   }

   public void setTargetMapID(int targetMapID) {
      this.targetMapID = targetMapID;
   }

   public int getTargetX() {
      return this.targetX;
   }

   public void setTargetX(int targetX) {
      this.targetX = targetX;
   }

   public int getTargetY() {
      return this.targetY;
   }

   public void setTargetY(int targetY) {
      this.targetY = targetY;
   }

   public long getId() {
      return this.id;
   }

   public int getWorldX() {
      return this.worldX;
   }

   public void setWorldX(int worldX) {
      this.worldX = worldX;
   }

   public int getWorldY() {
      return this.worldY;
   }

   public void setWorldY(int worldY) {
      this.worldY = worldY;
   }

   public void setReqest(String req) {
      this.reqStr = req;
   }

   public String getReqStr() {
      return this.reqStr;
   }
}
