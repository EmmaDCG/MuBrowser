package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class BossMenu extends DynamicMenu {
   public BossMenu() {
      super(9);
   }

   public int getShowNumber(Player player) {
      return 0;
   }

   public void onClick(Player player) {
      super.onClick(player);
   }

   public boolean hasEffect(Player player, int showNumber) {
      return false;
   }
}
