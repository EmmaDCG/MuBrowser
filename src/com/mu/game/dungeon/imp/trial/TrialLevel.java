package com.mu.game.dungeon.imp.trial;

public class TrialLevel {
   private int trialLevel;
   private int roleLevel;
   private String targetDes = "";

   public TrialLevel(int trialLevel, int roleLevel) {
      this.trialLevel = trialLevel;
      this.roleLevel = roleLevel;
   }

   public int getTrialLevel() {
      return this.trialLevel;
   }

   public int getRoleLevel() {
      return this.roleLevel;
   }

   public String getTargetDes() {
      return this.targetDes;
   }

   public void setTargetDes(String targetDes) {
      this.targetDes = targetDes;
   }
}
