package com.mu.game.model.unit.player.fcm;

public class PlayerPushInfo {
   private long id;
   private int pushId;
   private long lastPushTime;

   public PlayerPushInfo(long id) {
      this.id = id;
   }

   public int getPushId() {
      return this.pushId;
   }

   public void setPushId(int pushId) {
      this.pushId = pushId;
   }

   public long getLastPushTime() {
      return this.lastPushTime;
   }

   public void setLastPushTime(long lastPushTime) {
      this.lastPushTime = lastPushTime;
   }

   public long getId() {
      return this.id;
   }
}
