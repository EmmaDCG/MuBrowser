package com.mu.game.model.drop.model;

public class KillRecord {
   private int templateID;
   private int number;
   private boolean change = false;

   public KillRecord(int templateID, int number) {
      this.templateID = templateID;
      this.number = number;
   }

   public void addNumber() {
      ++this.number;
   }

   public boolean isChange() {
      return this.change;
   }

   public void setChange(boolean change) {
      this.change = change;
   }

   public int getTemplateID() {
      return this.templateID;
   }

   public void setTemplateID(int templateID) {
      this.templateID = templateID;
   }

   public int getNumber() {
      return this.number;
   }

   public void setNumber(int number) {
      this.number = number;
   }
}
