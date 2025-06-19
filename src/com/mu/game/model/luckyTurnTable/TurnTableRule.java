package com.mu.game.model.luckyTurnTable;

import com.mu.game.model.luckyTurnTable.weight.TurnTableElement;

public class TurnTableRule {
   private int count;
   private int needIngot;
   private TurnTableElement element;

   public TurnTableRule(int count, int needIngot, TurnTableElement element) {
      this.count = count;
      this.needIngot = needIngot;
      this.element = element;
   }

   public int getRndTableID() {
      return this.element.getRndTableID();
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public int getNeedIngot() {
      return this.needIngot;
   }

   public void setNeedIngot(int needIngot) {
      this.needIngot = needIngot;
   }

   public TurnTableElement getElement() {
      return this.element;
   }

   public void setElement(TurnTableElement element) {
      this.element = element;
   }
}
