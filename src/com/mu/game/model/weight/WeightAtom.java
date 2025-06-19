package com.mu.game.model.weight;

public class WeightAtom implements Comparable {
   private int weight;
   private int minWeight;
   private int maxWeight;

   public WeightAtom(int weight) {
      this.weight = weight;
   }

   public int getMinWeight() {
      return this.minWeight;
   }

   public void setMinWeight(int minWeight) {
      this.minWeight = minWeight;
   }

   public int getMaxWeight() {
      return this.maxWeight;
   }

   public void setMaxWeight(int maxWeight) {
      this.maxWeight = maxWeight;
   }

   public int getWeight() {
      return this.weight;
   }

   public void setWeight(int weight) {
      this.weight = weight;
   }

   public int compareTo(WeightAtom arg0) {
      return this.getWeight() - arg0.getWeight();
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
