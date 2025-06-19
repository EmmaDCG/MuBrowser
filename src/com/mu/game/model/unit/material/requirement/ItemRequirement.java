package com.mu.game.model.unit.material.requirement;

import com.mu.game.model.unit.player.Player;

public class ItemRequirement implements MaterialRequirement {
   private int itemModelID = 0;
   private int count = 1;

   public ItemRequirement(int modelID, int count) {
      this.itemModelID = modelID;
      this.count = count;
   }

   public int match(Player player) {
      return 1;
   }

   public synchronized void destroy() {
   }

   private String getItemName() {
      return "";
   }

   public String notMatchMessage(Player player) {
      return "";
   }

   public void endCollect(Player player) {
   }
}
