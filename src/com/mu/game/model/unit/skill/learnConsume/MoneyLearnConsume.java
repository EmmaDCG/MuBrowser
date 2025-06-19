package com.mu.game.model.unit.skill.learnConsume;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;

public class MoneyLearnConsume implements LearnConsume {
   private int money;

   public MoneyLearnConsume(int money) {
      this.money = money;
   }

   public void consumed(Player player) {
      PlayerManager.reduceMoney(player, this.money);
   }
}
