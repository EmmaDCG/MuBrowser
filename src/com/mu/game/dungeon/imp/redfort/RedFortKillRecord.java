package com.mu.game.dungeon.imp.redfort;

public class RedFortKillRecord implements Comparable {
   public static final int Status_Faild = 0;
   public static final int Status_Success = 1;
   public static final int Status_Draw = 2;
   private long rid;
   private String name;
   private int killNum = 0;
   private int top;
   private boolean hasReceived = false;
   private int status = 0;

   public RedFortKillRecord(long rid, String name) {
      this.rid = rid;
      this.name = name;
   }

   public int getKillNum() {
      return this.killNum;
   }

   public void setKillNum(int killNum) {
      this.killNum = killNum;
   }

   public long getRid() {
      return this.rid;
   }

   public String getName() {
      return this.name;
   }

   public int compareTo(RedFortKillRecord o) {
      return o.getKillNum() - this.getKillNum();
   }

   public int getTop() {
      return this.top;
   }

   public void setTop(int top) {
      this.top = top;
   }

   public boolean isHasReceived() {
      return this.hasReceived;
   }

   public void setHasReceived(boolean hasReceived) {
      this.hasReceived = hasReceived;
   }

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
