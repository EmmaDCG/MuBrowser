package com.mu.game.model.unit.controller;

import com.mu.game.model.unit.player.Player;

public interface CountdownObject {
   int BreakBeAttack = 1;
   int BreakDie = 2;

   void countdownEnd(Player var1);

   void stopCountDown(Player var1);

   int getTimeLength();

   String getCountDownName();

   boolean occupateStatus();

   int getCountdownType();

   int getBreakType();

   int getOrderType();
}
