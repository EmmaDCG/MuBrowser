package com.mu.game.model.equip.compositenew.condition;

import com.mu.game.model.item.Item;

public abstract class MaterialCondition {
   private int conID;

   public MaterialCondition(int conID) {
      this.conID = conID;
   }

   public abstract boolean suit(Item var1);

   public void setConID(int conID) {
      this.conID = conID;
   }

   public int getConID() {
      return this.conID;
   }
}
