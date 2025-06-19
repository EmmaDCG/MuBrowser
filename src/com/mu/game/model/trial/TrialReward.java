package com.mu.game.model.trial;

public class TrialReward {
   public static final int Free = 0;
   public static final int Money = 1;
   public static final int Ingot = 2;
   private int times = 0;
   private int consumeType = 0;
   private int consume = 0;
   private String description = "";

   public TrialReward(int times) {
      this.times = times;
   }

   public int getConsumeType() {
      return this.consumeType;
   }

   public void setConsumeType(int consumeType) {
      this.consumeType = consumeType;
   }

   public int getConsume() {
      return this.consume;
   }

   public void setConsume(int consume) {
      this.consume = consume;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public int getTimes() {
      return this.times;
   }
}
