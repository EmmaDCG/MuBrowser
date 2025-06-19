package com.mu.game.model.drop.model;

public class MDropAtom implements Comparable {
   private int groupID;
   private boolean isBind;
   private boolean isRate;
   private int controlID = -1;
   private long minWeight;
   private long maxWeight;
   private long weight;
   private int statRuleID;

   public MDropAtom(int groupID, long weight, boolean isBind, boolean isRate, int controlID, int statRuleID) {
      this.weight = weight;
      this.groupID = groupID;
      this.isBind = isBind;
      this.isRate = isRate;
      this.controlID = controlID;
      this.statRuleID = statRuleID;
   }

   public long getWeight() {
      return this.weight;
   }

   public int compareTo(MDropAtom arg0) {
      long com = this.getWeight() - arg0.getWeight();
      if (com >= 1L) {
         return 1;
      } else {
         return com == 0L ? 0 : -1;
      }
   }

   public long getMinWeight() {
      return this.minWeight;
   }

   public void setMinWeight(long minWeight) {
      this.minWeight = minWeight;
   }

   public long getMaxWeight() {
      return this.maxWeight;
   }

   public void setMaxWeight(long maxWeight) {
      this.maxWeight = maxWeight;
   }

   public int getProtectedTime() {
      return DropControl.getProtectedTime(this.controlID);
   }

   public int getGroupID() {
      return this.groupID;
   }

   public void setGroupID(int groupID) {
      this.groupID = groupID;
   }

   public boolean isBind() {
      return this.isBind;
   }

   public void setBind(boolean isBind) {
      this.isBind = isBind;
   }

   public boolean isRate() {
      return this.isRate;
   }

   public void setRate(boolean isRate) {
      this.isRate = isRate;
   }

   public int getControlID() {
      return this.controlID;
   }

   public void setControlID(int controlID) {
      this.controlID = controlID;
   }

   public int getStatRuleID() {
      return this.statRuleID;
   }

   public void setStatRuleID(int statRuleID) {
      this.statRuleID = statRuleID;
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
