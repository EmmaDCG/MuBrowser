package com.mu.game.dungeon.imp.bloodcastle;

public class ScheduleConditions {
   private int schedule;
   private String explain;
   private int[] target;
   private int maxNumber;

   public ScheduleConditions(int schedule) {
      this.schedule = schedule;
   }

   public String getExplain() {
      return this.explain;
   }

   public void setExplain(String explain) {
      this.explain = explain;
   }

   public int[] getTarget() {
      return this.target;
   }

   public void setTarget(int[] target) {
      this.target = target;
   }

   public int getMaxNumber() {
      return this.maxNumber;
   }

   public void setMaxNumber(int maxNumber) {
      this.maxNumber = maxNumber;
   }

   public int getSchedule() {
      return this.schedule;
   }

   public boolean contains(int t) {
      if (this.target != null) {
         for(int i = 0; i < this.target.length; ++i) {
            if (this.target[i] == t) {
               return true;
            }
         }
      }

      return false;
   }
}
