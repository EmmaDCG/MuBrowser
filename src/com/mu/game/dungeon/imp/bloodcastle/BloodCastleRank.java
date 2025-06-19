package com.mu.game.dungeon.imp.bloodcastle;

public class BloodCastleRank {
   private int rank;
   private int maxTime;
   private String target;
   private int timeLeft;
   private int actualTime;

   public int getRank() {
      return this.rank;
   }

   public void setRank(int rank) {
      this.rank = rank;
   }

   public int getMaxtTime() {
      return this.maxTime;
   }

   public void setMaxTime(int time) {
      this.maxTime = time;
   }

   public String getTarget() {
      return this.target;
   }

   public void setTarget(String target) {
      this.target = target;
   }

   public int getTimeLeft() {
      return this.timeLeft;
   }

   public void setTimeLeft(int timeLeft) {
      this.timeLeft = timeLeft;
   }

   public int getActualTime() {
      return this.actualTime;
   }

   public void setActualTime(int actualTime) {
      this.actualTime = actualTime;
   }
}
