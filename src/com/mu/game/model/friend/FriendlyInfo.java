package com.mu.game.model.friend;

public class FriendlyInfo {
   private int level;
   private int minExp;
   private int maxExp;
   private int expBouns = 1;

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getMinExp() {
      return this.minExp;
   }

   public void setMinExp(int minExp) {
      this.minExp = minExp;
   }

   public int getMaxExp() {
      return this.maxExp;
   }

   public void setMaxExp(int maxExp) {
      this.maxExp = maxExp;
   }

   public int getExpBouns() {
      return this.expBouns;
   }

   public void setExpBouns(int expBouns) {
      this.expBouns = expBouns;
   }
}
