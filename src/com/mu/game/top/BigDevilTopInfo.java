package com.mu.game.top;

public class BigDevilTopInfo extends TopInfo implements Comparable {
   private long exp;
   private long time;
   private int dunId;
   private int dunLevel;

   public BigDevilTopInfo(long exp, long time) {
      this.exp = exp;
      this.time = time;
   }

   public String getvariable() {
      return null;
   }

   public long getExp() {
      return this.exp;
   }

   public void setExp(long exp) {
      this.exp = exp;
   }

   public long getTime() {
      return this.time;
   }

   public void setTime(long time) {
      this.time = time;
   }

   public int getDunId() {
      return this.dunId;
   }

   public void setDunId(int dunId) {
      this.dunId = dunId;
   }

   public int getDunLevel() {
      return this.dunLevel;
   }

   public void setDunLevel(int dunLevel) {
      this.dunLevel = dunLevel;
   }

   public int compareTo(BigDevilTopInfo o) {
      if (o.getExp() > this.getExp()) {
         return 1;
      } else if (this.getExp() == o.getExp()) {
         return this.getTime() - o.getTime() < 0L ? 0 : 1;
      } else {
         return 0;
      }
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
