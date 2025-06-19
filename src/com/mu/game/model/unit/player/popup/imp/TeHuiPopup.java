package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.imp.tehui.ActivityTeHuiElement;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class TeHuiPopup extends Popup {
   private int activityElementId;

   public TeHuiPopup(int id, int eid) {
      super(id);
      this.activityElementId = eid;
   }

   public String getTitle() {
      return MessageText.getText(25103);
   }

   public String getContent() {
      ActivityElement ae = ActivityManager.getActivityElement(this.activityElementId);
      return ae != null ? MessageText.getText(25104).replace("%s%", String.valueOf(((ActivityTeHuiElement)ae).getIngot())) : "";
   }

   public void dealLeftClick(Player player) {
      ActivityElement ae = ActivityManager.getActivityElement(this.activityElementId);
      if (ae != null) {
         ((ActivityTeHuiElement)ae).receive(player);
      }

   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return true;
   }

   public int getType() {
      return 29;
   }
}
