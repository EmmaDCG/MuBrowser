package com.mu.game.model.drop.model.control;

import com.mu.game.model.drop.model.DropControl;
import com.mu.game.model.unit.player.Player;

public class ActivityControl extends DropControl {
   private int activityID;

   public ActivityControl(int controlID, int type, int targetID) {
      super(controlID, type);
      this.activityID = targetID;
   }

   public boolean checkDrop(Player player, int templateID) {
      return false;
   }

   public int getCanDropCount(Player player, int itemModelID) {
      return 2147483646;
   }

   public int getActivityID() {
      return this.activityID;
   }

   public void setActivityID(int activityID) {
      this.activityID = activityID;
   }

   public static ActivityControl createActivityControl(int controlID, int type, int targetID, String value) {
      ActivityControl ac = new ActivityControl(controlID, type, targetID);
      return ac;
   }
}
