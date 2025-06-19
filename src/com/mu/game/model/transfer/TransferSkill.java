package com.mu.game.model.transfer;

public class TransferSkill {
   private int transfer;
   private int job;
   private String name;
   private int icon;
   private String tip;

   public int getTransfer() {
      return this.transfer;
   }

   public void setTransfer(int transfer) {
      this.transfer = transfer;
   }

   public int getJob() {
      return this.job;
   }

   public void setJob(int job) {
      this.job = job;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public String getTip() {
      return this.tip;
   }

   public void setTip(String tip) {
      this.tip = tip;
   }
}
