package com.mu.game.model.unit.player.offline;

public class OfflineBuffInfo {
   private int vipLevel;
   private int buffId;
   private int buffLevel;
   private int addition = 100;

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

   public int getAddition() {
      return this.addition;
   }

   public void setAddition(int addition) {
      this.addition = addition;
   }

   public int getVipLevel() {
      return this.vipLevel;
   }

   public void setVipLevel(int vipLevel) {
      this.vipLevel = vipLevel;
   }
}
