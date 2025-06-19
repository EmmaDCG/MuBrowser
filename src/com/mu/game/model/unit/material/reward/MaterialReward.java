package com.mu.game.model.unit.material.reward;

import com.mu.game.model.unit.player.Player;

public interface MaterialReward {
   int Reward_Item = 1;

   int doReword(Player var1);

   void destroy();

   int canReward(Player var1);
}
