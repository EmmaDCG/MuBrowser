package com.mu.game.model.unit.material.requirement;

import com.mu.game.model.unit.player.Player;

public interface MaterialRequirement {
   int Requirement_Item = 1;
   int Requirement_Task = 2;

   int match(Player var1);

   String notMatchMessage(Player var1);

   void endCollect(Player var1);

   void destroy();
}
