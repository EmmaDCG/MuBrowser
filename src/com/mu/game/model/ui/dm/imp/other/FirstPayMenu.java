package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class FirstPayMenu extends DynamicMenu {
   public FirstPayMenu() {
      super(16);
   }

   public ActivityElement getActivityElement() {
      Activity activity = ActivityManager.getActivity(1);
      return activity != null ? (ActivityElement)activity.getElementList().get(0) : null;
   }

   public int getShowNumber(Player player) {
      return 0;
   }

   public boolean hasEffect(Player player, int showNumber) {
      ActivityElement element = this.getActivityElement();
      return element == null ? false : element.getReceiveStatus(player) == 1;
   }

   public boolean isShow(Player player) {
      if (!super.isShow(player)) {
         return false;
      } else {
         ActivityElement element = this.getActivityElement();
         return element == null ? false : element.getFather().isOpen() && element.getReceiveStatus(player) != 2;
      }
   }
}
