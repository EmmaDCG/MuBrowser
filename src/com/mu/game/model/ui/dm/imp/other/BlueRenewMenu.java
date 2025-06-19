package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.imp.tx.bluerenew.ActivityBlueRenew;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class BlueRenewMenu extends DynamicMenu {
   public BlueRenewMenu() {
      super(28);
   }

   public int getShowNumber(Player player) {
      return 0;
   }

   public boolean isShow(Player player) {
      ActivityBlueRenew renew = ActivityManager.getBlueRenew();
      return renew != null && renew.isOpen() && renew.hasPlayer(player.getID()) && renew.getElement() != null && renew.getElement().getReceiveStatus(player) != 2;
   }

   public boolean hasEffect(Player player, int showNumber) {
      return true;
   }
}
