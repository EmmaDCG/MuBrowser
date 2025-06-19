package com.mu.game.model.spiritOfWar.model;

import java.util.HashMap;
import java.util.List;

public class SpiritModel {
   private int rank;
   private int level;
   private long needExp;
   private List stats;
   private int domineering;
   private int ingotExp;
   private HashMap itemCount = null;
   private int addDomineering = 0;

   public SpiritModel(int rank, int level, long needExp) {
      this.rank = rank;
      this.level = level;
      this.needExp = needExp;
   }

   public int getCanUseCount(int itemID) {
      return this.itemCount.containsKey(itemID) ? ((Integer)this.itemCount.get(itemID)).intValue() : 0;
   }

   public int getRank() {
      return this.rank;
   }

   public void setRank(int rank) {
      this.rank = rank;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public long getNeedExp() {
      return this.needExp;
   }

   public void setNeedExp(long needExp) {
      this.needExp = needExp;
   }

   public List getStats() {
      return this.stats;
   }

   public void setStats(List stats) {
      this.stats = stats;
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }

   public int getIngotExp() {
      return this.ingotExp;
   }

   public void setIngotExp(int ingotExp) {
      this.ingotExp = ingotExp;
   }

   public HashMap getItemCount() {
      return this.itemCount;
   }

   public void setItemCount(HashMap itemCount) {
      this.itemCount = itemCount;
   }

   public int getAddDomineering() {
      return this.addDomineering;
   }

   public void setAddDomineering(int addDomineering) {
      this.addDomineering = addDomineering;
   }
}
