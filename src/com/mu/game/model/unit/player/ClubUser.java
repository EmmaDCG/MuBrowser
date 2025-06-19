package com.mu.game.model.unit.player;

public class ClubUser {
   private int memberLevel = -1;
   private int vipLevel = -1;
   private long memberExpireTime = -1L;
   private boolean isAnnualMember = false;

   public boolean isMember() {
      return this.memberLevel > 0;
   }

   public boolean isVip() {
      return this.vipLevel > 0;
   }

   public int getMemberLevel() {
      return this.memberLevel;
   }

   public void setMemberLevel(int memberLevel) {
      this.memberLevel = memberLevel;
   }

   public int getVipLevel() {
      return this.vipLevel;
   }

   public void setVipLevel(int vipLevel) {
      this.vipLevel = vipLevel;
   }

   public long getMemberExpireTime() {
      return this.memberExpireTime;
   }

   public void setMemberExpireTime(long memberExpireTime) {
      this.memberExpireTime = memberExpireTime;
   }

   public boolean isAnnualMember() {
      return this.isAnnualMember;
   }

   public void setAnnualMember(boolean isAnnualMember) {
      this.isAnnualMember = isAnnualMember;
   }
}
