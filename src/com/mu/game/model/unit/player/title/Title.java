package com.mu.game.model.unit.player.title;

public class Title {
   private int id;
   private long expiredTime = -1L;
   private boolean isEquip = false;

   public Title(int id) {
      this.id = id;
   }

   public int getId() {
      return this.id;
   }

   public long getExpiredTime() {
      return this.expiredTime;
   }

   public void setExpiredTime(long expiredTime) {
      this.expiredTime = expiredTime;
   }

   public boolean isEquip() {
      return this.isEquip;
   }

   public void setEquip(boolean isEquip) {
      this.isEquip = isEquip;
   }
}
