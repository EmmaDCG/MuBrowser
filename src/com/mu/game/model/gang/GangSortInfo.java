package com.mu.game.model.gang;

public class GangSortInfo implements Comparable {
   private long gangId = 0L;
   private long contribution = 0L;
   private int applyStatus = 0;

   public GangSortInfo(long gangId) {
      this.gangId = gangId;
   }

   public long getGangId() {
      return this.gangId;
   }

   public long getContribution() {
      return this.contribution;
   }

   public void setContribution(long contribution) {
      this.contribution = contribution;
   }

   public int getApplyStatus() {
      return this.applyStatus;
   }

   public void setApplyStatus(int applyStatus) {
      this.applyStatus = applyStatus;
   }

   public int compareTo(GangSortInfo o) {
      if (this.applyStatus < o.getApplyStatus()) {
         return 1;
      } else {
         return this.contribution < o.getContribution() ? 1 : -1;
      }
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
