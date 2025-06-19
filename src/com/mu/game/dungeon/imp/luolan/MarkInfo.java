package com.mu.game.dungeon.imp.luolan;

public class MarkInfo implements Comparable {
   private long gangId;
   private int times;
   private long markTime;

   public int compareTo(MarkInfo o) {
      int result = o.getTimes() - this.getTimes();
      if (result != 0) {
         return result;
      } else {
         return o.getMarkTime() > this.getMarkTime() ? -1 : 1;
      }
   }

   public long getGangId() {
      return this.gangId;
   }

   public void setGangId(long gangId) {
      this.gangId = gangId;
   }

   public int getTimes() {
      return this.times;
   }

   public void setTimes(int times) {
      this.times = times;
   }

   public long getMarkTime() {
      return this.markTime;
   }

   public void setMarkTime(long markTime) {
      this.markTime = markTime;
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
