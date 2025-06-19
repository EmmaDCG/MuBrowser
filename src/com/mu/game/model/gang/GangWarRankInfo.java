package com.mu.game.model.gang;

public class GangWarRankInfo {
   private long id;
   private int totalLevel;
   private int flag;
   private String name;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public int getTotalLevel() {
      return this.totalLevel;
   }

   public void setTotalLevel(int tLevel) {
      this.totalLevel = tLevel;
   }

   public int getFlag() {
      return this.flag;
   }

   public void setFlag(int flag) {
      this.flag = flag;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
