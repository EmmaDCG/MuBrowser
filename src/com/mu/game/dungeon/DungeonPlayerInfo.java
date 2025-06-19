package com.mu.game.dungeon;

public class DungeonPlayerInfo {
   private long rid;
   private boolean hasRewad = false;
   private int lifeInspireLevel = 0;
   private int hurtInspireLevel = 0;
   private long exp = 0L;
   private int killNum = 0;
   private int costTime = 0;

   public long getRid() {
      return this.rid;
   }

   public void setRid(long rid) {
      this.rid = rid;
   }

   public boolean hasRewad() {
      return this.hasRewad;
   }

   public void setHasRewad(boolean hasRewad) {
      this.hasRewad = hasRewad;
   }

   public int getLifeInspireLevel() {
      return this.lifeInspireLevel;
   }

   public void setLifeInspireLevel(int lifeInspireLevel) {
      this.lifeInspireLevel = lifeInspireLevel;
   }

   public int getHurtInspireLevel() {
      return this.hurtInspireLevel;
   }

   public void setHurtInspireLevel(int hurtInspireLevel) {
      this.hurtInspireLevel = hurtInspireLevel;
   }

   public int getKillNum() {
      return this.killNum;
   }

   public void addKillNum() {
      ++this.killNum;
   }

   public long getExp() {
      return this.exp;
   }

   public void addExp(long e) {
      this.exp += e;
   }

   public int getCostTime() {
      return this.costTime;
   }

   public void addCostTime(int time) {
      this.costTime += time;
   }

   public void setCostTime(int costTime) {
      this.costTime = costTime;
   }
}
