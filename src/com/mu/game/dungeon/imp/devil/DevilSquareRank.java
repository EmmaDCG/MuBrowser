package com.mu.game.dungeon.imp.devil;

public class DevilSquareRank {
   private int rank;
   private int minKill;
   private int maxKill;
   private String target;
   private int buffId = -1;
   private int buffLevel = 0;
   private String expAdditionDes;
   private String levelUpStr;

   public int getRank() {
      return this.rank;
   }

   public void setRank(int rank) {
      this.rank = rank;
   }

   public int getMinKill() {
      return this.minKill;
   }

   public void setMinKill(int minKill) {
      this.minKill = minKill;
   }

   public int getMaxKill() {
      return this.maxKill;
   }

   public void setMaxKill(int maxKill) {
      this.maxKill = maxKill;
   }

   public String getTarget() {
      return this.target;
   }

   public void setTarget(String target) {
      this.target = target;
   }

   public int getBuffId() {
      return this.buffId;
   }

   public void setBuffId(int buffId) {
      this.buffId = buffId;
   }

   public int getBuffLevel() {
      return this.buffLevel;
   }

   public void setBuffLevel(int buffLevel) {
      this.buffLevel = buffLevel;
   }

   public String getExpAdditionDes() {
      return this.expAdditionDes;
   }

   public void setExpAdditionDes(String expAdditionDes) {
      this.expAdditionDes = expAdditionDes;
   }

   public String getLevelUpStr() {
      return this.levelUpStr;
   }

   public void setLevelUpStr(String levelUpStr) {
      this.levelUpStr = levelUpStr;
   }
}
