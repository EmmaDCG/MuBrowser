package com.mu.game.model.unit.player.dun;

public class DunLogs {
   private int dunId;
   private int smallId;
   private int finishTimes = 0;
   private long lastFinishTime = 0L;
   private long saveDay = 0L;
   private boolean hasReceived = false;
   private long baseExp = 0L;
   private int baseMoney = 0;
   private int vipLevel = 0;

   public DunLogs(int dunId) {
      this.dunId = dunId;
   }

   public int getSmallId() {
      return this.smallId;
   }

   public void setSmallId(int smallId) {
      this.smallId = smallId;
   }

   public boolean notReceivd() {
      return !this.hasReceived && (this.baseExp > 0L || this.baseMoney > 0);
   }

   public int getDunId() {
      return this.dunId;
   }

   public int getFinishTimes() {
      return this.finishTimes;
   }

   public void setFinishTimes(int finishTimes) {
      this.finishTimes = finishTimes;
   }

   public long getLastFinishTime() {
      return this.lastFinishTime;
   }

   public void setLastFinishTime(long lastFinishTime) {
      this.lastFinishTime = lastFinishTime;
   }

   public long getSaveDay() {
      return this.saveDay;
   }

   public void setSaveDay(long saveDay) {
      this.saveDay = saveDay;
   }

   public boolean isHasReceived() {
      return this.hasReceived;
   }

   public void setHasReceived(boolean hasReceived) {
      this.hasReceived = hasReceived;
   }

   public long getBaseExp() {
      return this.baseExp;
   }

   public void setBaseExp(long baseExp) {
      this.baseExp = baseExp;
   }

   public int getBaseMoney() {
      return this.baseMoney;
   }

   public void setBaseMoney(int baseMoney) {
      this.baseMoney = baseMoney;
   }

   public int getVipLevel() {
      return this.vipLevel;
   }

   public void setVipLevel(int vipLevel) {
      this.vipLevel = vipLevel;
   }
}
