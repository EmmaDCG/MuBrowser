package com.mu.game.model.gang;

public class GangNotice {
   private long gangId;
   private long time;
   private String detail;

   public long getGangId() {
      return this.gangId;
   }

   public void setGangId(long gangId) {
      this.gangId = gangId;
   }

   public long getTime() {
      return this.time;
   }

   public void setTime(long time) {
      this.time = time;
   }

   public String getDetail() {
      return this.detail;
   }

   public void setDetail(String detail) {
      this.detail = detail;
   }
}
