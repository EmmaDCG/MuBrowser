package com.mu.game.model.ui.dm.imp.other;

import com.mu.config.Global;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class BlueVipMenu extends DynamicMenu {
   public BlueVipMenu() {
      super(26);
   }

   public int getShowNumber(Player player) {
      return ActivityManager.getBlueVipShwoNumber(player);
   }

   public boolean isShow(Player player) {
      return Global.getParserID() == 5;
   }

   public boolean hasEffect(Player player, int showNumber) {
      return showNumber > 0;
   }
}
