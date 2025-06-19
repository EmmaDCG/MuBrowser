package com.mu.game.dungeon.imp.bigdevil;

public class BigDevilResultInfo {
   private long rid;
   private String name;
   private int squareLevel;
   private long time;
   private long exp;

   public long getRid() {
      return this.rid;
   }

   public void setRid(long rid) {
      this.rid = rid;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getSquareLevel() {
      return this.squareLevel;
   }

   public void setSquareLevel(int squareLevel) {
      this.squareLevel = squareLevel;
   }

   public long getTime() {
      return this.time;
   }

   public void setTime(long time) {
      this.time = time;
   }

   public long getExp() {
      return this.exp;
   }

   public void setExp(long exp) {
      this.exp = exp;
   }
}
