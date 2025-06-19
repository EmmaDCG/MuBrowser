package com.mu.game.model.unit.action;

import com.mu.game.model.unit.player.Player;

public interface Action {
   void doAction(Player var1);

   void destroy();
}
