package com.mu.game.dungeon.imp.discovery;

public class DiscoveryInfo {
   private int discoveyId;
   private int minRate = 0;
   private int maxRate = 0;
   private int mapId;
   private int x;
   private int y;
   private String mapName;
   private int sourceRate = 0;
   private int dunType = 1;
   private String rightDes = "";

   public int getDiscoveyId() {
      return this.discoveyId;
   }

   public void setDiscoveyId(int discoveyId) {
      this.discoveyId = discoveyId;
   }

   public int getMinRate() {
      return this.minRate;
   }

   public void setMinRate(int rate) {
      this.minRate = rate;
   }

   public int getMaxRate() {
      return this.maxRate;
   }

   public void setMaxRate(int maxRate) {
      this.maxRate = maxRate;
   }

   public int getMapId() {
      return this.mapId;
   }

   public void setMapId(int mapId) {
      this.mapId = mapId;
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

   public String getMapName() {
      return this.mapName;
   }

   public void setMapName(String mapName) {
      this.mapName = mapName;
   }

   public int getSourceRate() {
      return this.sourceRate;
   }

   public int getDunType() {
      return this.dunType;
   }

   public void setDunType(int dunType) {
      this.dunType = dunType;
   }

   public void setSourceRate(int sourceRate) {
      this.sourceRate = sourceRate;
   }

   public String getRightDes() {
      return this.rightDes;
   }

   public void setRightDes(String rightDes) {
      this.rightDes = rightDes;
   }
}
