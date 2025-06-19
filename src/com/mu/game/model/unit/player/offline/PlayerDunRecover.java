package com.mu.game.model.unit.player.offline;

public class PlayerDunRecover {
   private int dunid;
   private long recoverDay;
   private int recoverTimes = 0;
   private int remainderTimes = 0;

   public PlayerDunRecover(int dunId) {
      this.dunid = dunId;
   }

   public int getDunId() {
      return this.dunid;
   }

   public long getRecoverDay() {
      return this.recoverDay;
   }

   public void setRecoverDay(long recoverDay) {
      this.recoverDay = recoverDay;
   }

   public int getRecoverTimes() {
      return this.recoverTimes;
   }

   public void setRecoverTimes(int recoverTimes) {
      this.recoverTimes = recoverTimes;
   }

   public int getRemainderTimes() {
      return this.remainderTimes;
   }

   public void setRemainderTimes(int remainderTimes) {
      this.remainderTimes = remainderTimes;
   }
}
