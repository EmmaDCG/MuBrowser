package com.mu.game.model.unit.material.reward;

import com.mu.game.model.unit.player.Player;

public class ItemReward implements MaterialReward {
   private int itemModelID = 0;
   private int count = 0;
   private boolean isBind = true;

   public ItemReward(int modelID, int count) {
      this.itemModelID = modelID;
      this.count = count;
   }

   public void destroy() {
   }

   public int doReword(Player player) {
      return player.getItemManager().addItem(this.itemModelID, this.count, this.isBind, 22).getResult();
   }

   public int canReward(Player player) {
      return 1;
   }

   public boolean isBind() {
      return this.isBind;
   }

   public void setBind(boolean isBind) {
      this.isBind = isBind;
   }
}
