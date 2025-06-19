package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class OfflineMenu extends DynamicMenu {
   public OfflineMenu() {
      super(30);
   }

   public int getShowNumber(Player player) {
      return 0;
   }

   public boolean hasEffect(Player player, int showNumber) {
      return false;
   }
}
