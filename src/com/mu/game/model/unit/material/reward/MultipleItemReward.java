package com.mu.game.model.unit.material.reward;

import com.mu.game.model.unit.player.Player;
import java.util.ArrayList;

public class MultipleItemReward implements MaterialReward {
   public MultipleItemReward(ArrayList itemList) {
   }

   public int canReward(Player player) {
      return 1;
   }

   public void destroy() {
   }

   public int doReword(Player player) {
      return 1;
   }
}
