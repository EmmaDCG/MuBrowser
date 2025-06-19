package com.mu.game.dungeon.imp.luolan;

import com.mu.game.model.unit.controller.CountdownObject;
import com.mu.game.model.unit.player.Player;

public class GearManCountDown implements CountdownObject {
   private LuolanMap map = null;
   private long time = 5000L;
   private String text = "";

   public GearManCountDown(LuolanMap map, long time, String text) {
      this.map = map;
      this.text = text;
      this.time = time;
   }

   public LuolanMap getMap() {
      return this.map;
   }

   public void countdownEnd(Player player) {
      this.getMap().lostGear();
      this.getMap().refreshBattleInfo();
   }

   public void stopCountDown(Player player) {
   }

   public int getTimeLength() {
      return (int)this.time;
   }

   public String getCountDownName() {
      return this.text;
   }

   public boolean occupateStatus() {
      return false;
   }

   public int getCountdownType() {
      return 5;
   }

   public int getBreakType() {
      return 2;
   }

   public int getOrderType() {
      return 1;
   }
}
