package com.mu.game.model.drop.model;

import com.mu.game.model.drop.model.control.ActivityControl;
import com.mu.game.model.drop.model.control.FirstKillControl;
import com.mu.game.model.drop.model.control.KillNumberControl;
import com.mu.game.model.drop.model.control.ProfessionControl;
import com.mu.game.model.drop.model.control.TaskControl;
import com.mu.game.model.unit.player.Player;
import java.util.HashMap;

public abstract class DropControl {
   private static HashMap controls = new HashMap();
   private int controlID;
   private int type;

   public DropControl(int controlID, int type) {
      this.controlID = controlID;
      this.type = type;
   }

   public abstract boolean checkDrop(Player var1, int var2);

   public abstract int getCanDropCount(Player var1, int var2);

   public static DropControl getControl(int controlID) {
      return (DropControl)controls.get(controlID);
   }

   public static int getProtectedTime(int controlID) {
      DropControl control = getControl(controlID);
      if (control == null) {
         return -1;
      } else {
         return control.getType() == 2 ? 120000 : -1;
      }
   }

   public static DropControl createControl(int controlID, int type, int targetID, String value) throws Exception {
      DropControl dc = null;
      switch(type) {
      case 1:
         dc = ActivityControl.createActivityControl(controlID, type, targetID, value);
         break;
      case 2:
         dc = TaskControl.createTaskControl(controlID, type, targetID, value);
         break;
      case 3:
         dc = FirstKillControl.createFirstKillControl(controlID, type, targetID, value);
         break;
      case 4:
         dc = KillNumberControl.createKillNumberControl(controlID, type, targetID, value);
         break;
      case 5:
         dc = ProfessionControl.createProControl(controlID, type, targetID, value);
      }

      return (DropControl)dc;
   }

   public static HashMap getControls() {
      return controls;
   }

   public int getControlID() {
      return this.controlID;
   }

   public void setControlID(int controlID) {
      this.controlID = controlID;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }
}
