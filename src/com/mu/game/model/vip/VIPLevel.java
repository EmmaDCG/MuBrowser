package com.mu.game.model.vip;

public class VIPLevel implements Comparable {
   private int level;
   private int maxExp;
   private VIPLevel next;

   public VIPLevel(int level, int maxExp) {
      this.level = level;
      this.maxExp = maxExp;
   }

   public int getLevel() {
      return this.level;
   }

   public int getMaxExp() {
      return this.maxExp;
   }

   public static int getMaxLevel() {
      return 0;
   }

   public int compareTo(VIPLevel o) {
      return this.getLevel() < o.getLevel() ? -1 : 1;
   }

   public VIPLevel getNext() {
      return this.next;
   }

   public void setNext(VIPLevel next) {
      this.next = next;
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
