package com.mu.game.model.unit.skill.learnCondition;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;

public class MoneyCondition implements LearnCondition {
   private int value;

   public MoneyCondition(int value) {
      this.value = value;
   }

   public int verify(Player player) {
      int result = PlayerManager.hasEnoughMoney(player, this.value) ? 1 : 1011;
      return result;
   }

   public int getType() {
      return 1;
   }
}
