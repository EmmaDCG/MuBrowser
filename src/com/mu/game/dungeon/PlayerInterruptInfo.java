package com.mu.game.dungeon;

public class PlayerInterruptInfo {
   private long playerID;
   private int dungeonID;
   private int mapIndex;
   private int x;
   private int y;
   private long interruptTime = System.currentTimeMillis();
   private int lifeInspireLevel = 0;
   private int hurtInspireLevel = 0;

   public PlayerInterruptInfo(long pid, int dungeonID, int mapIndex) {
      this.playerID = pid;
      this.dungeonID = dungeonID;
      this.mapIndex = mapIndex;
   }

   public long getPlayerID() {
      return this.playerID;
   }

   public int getDungeonID() {
      return this.dungeonID;
   }

   public int getMapIndex() {
      return this.mapIndex;
   }

   public long getInterruptTime() {
      return this.interruptTime;
   }

   public void setInterruptTime(long interruptTime) {
      this.interruptTime = interruptTime;
   }

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getLifeInspireLevel() {
      return this.lifeInspireLevel;
   }

   public void setLifeInspireLevel(int lifeInspireLevel) {
      this.lifeInspireLevel = lifeInspireLevel;
   }

   public int getHurtInspireLevel() {
      return this.hurtInspireLevel;
   }

   public void setHurtInspireLevel(int hurtInspireLevel) {
      this.hurtInspireLevel = hurtInspireLevel;
   }
}
