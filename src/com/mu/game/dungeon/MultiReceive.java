package com.mu.game.dungeon;

import com.mu.game.model.unit.player.Player;

public interface MultiReceive {
   int Single_Receive = 1;
   int Double_Receive = 2;

   boolean doReceive(Player var1, int var2);
}
