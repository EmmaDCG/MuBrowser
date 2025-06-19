package com.mu.game.model.luckyTurnTable.weight;

import com.mu.game.model.weight.WeightAtom;

public class TurnTableAtom extends WeightAtom {
   private int tableID;

   public TurnTableAtom(int weight, int tableID) {
      super(weight);
      this.tableID = tableID;
   }

   public int getTableID() {
      return this.tableID;
   }

   public void setTableID(int tableID) {
      this.tableID = tableID;
   }
}
