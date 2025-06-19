package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class CloseTestMenu extends DynamicMenu {
   ActivityElement ae = null;

   public CloseTestMenu() {
      super(17);
   }

   public ActivityElement getActivityElement() {
      if (this.ae == null) {
         Activity activity = ActivityManager.getActivity(5);
         if (activity != null) {
            this.ae = (ActivityElement)activity.getElementList().get(0);
         }
      }

      return this.ae;
   }

   public int getShowNumber(Player player) {
      ActivityElement element = this.getActivityElement();
      return element != null && element.getReceiveStatus(player) == 1 ? 1 : 0;
   }

   public boolean hasEffect(Player player, int showNumber) {
      ActivityElement element = this.getActivityElement();
      return element == null ? false : element.getReceiveStatus(player) == 1;
   }

   public boolean isShow(Player player) {
      return this.getActivityElement() != null && this.getActivityElement().getFather().isOpen();
   }
}
