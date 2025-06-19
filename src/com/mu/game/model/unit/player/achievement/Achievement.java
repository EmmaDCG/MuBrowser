package com.mu.game.model.unit.player.achievement;

public class Achievement {
   private int id;
   private int needNumber;
   private boolean saveAtOnce = false;

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getNeedNumber() {
      return this.needNumber;
   }

   public void setNeedNumber(int needNumber) {
      this.needNumber = needNumber;
   }

   public boolean isFinished(int num) {
      return num >= this.needNumber;
   }

   public boolean isSaveAtOnce() {
      return this.saveAtOnce;
   }

   public void setSaveAtOnce(boolean saveAtOnce) {
      this.saveAtOnce = saveAtOnce;
   }
}
