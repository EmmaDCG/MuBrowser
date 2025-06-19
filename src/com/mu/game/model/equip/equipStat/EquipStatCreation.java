package com.mu.game.model.equip.equipStat;

import com.mu.game.model.equip.weight.CreationElement;

public abstract class EquipStatCreation {
   private int sortID;
   private String des;
   private CreationElement element;

   public EquipStatCreation(int sortID) {
      this.sortID = sortID;
   }

   public int getRndValue() {
      return this.element.getRndValue();
   }

   public int getBestValue() {
      return this.element.getBestValue();
   }

   public int getSortID() {
      return this.sortID;
   }

   public void setSortID(int sortID) {
      this.sortID = sortID;
   }

   public CreationElement getElement() {
      return this.element;
   }

   public void setElement(CreationElement element) {
      this.element = element;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public String getDes() {
      return this.des;
   }
}
