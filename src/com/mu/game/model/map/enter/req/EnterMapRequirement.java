package com.mu.game.model.map.enter.req;

import com.mu.game.model.unit.player.Player;

public interface EnterMapRequirement {
   boolean canEnter(Player var1, boolean var2);
}
