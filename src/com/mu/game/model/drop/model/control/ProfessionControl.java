package com.mu.game.model.drop.model.control;

import com.mu.game.model.drop.model.DropControl;
import com.mu.game.model.unit.player.Player;

public class ProfessionControl extends DropControl {
   private int proType;

   public ProfessionControl(int controlID, int type, int targetID) {
      super(controlID, type);
      this.proType = targetID;
   }

   public boolean checkDrop(Player player, int templateID) {
      return player.getProType() == this.getProType();
   }

   public int getCanDropCount(Player player, int itemModelID) {
      return 2147483646;
   }

   public int getProType() {
      return this.proType;
   }

   public void setProType(int proType) {
      this.proType = proType;
   }

   public static ProfessionControl createProControl(int controlID, int type, int targetID, String value) {
      ProfessionControl pc = new ProfessionControl(controlID, type, targetID);
      return pc;
   }
}
