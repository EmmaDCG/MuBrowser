package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class FinancingMenu extends DynamicMenu {
   public FinancingMenu() {
      super(14);
   }

   public boolean isShow(Player player) {
      return super.isShow(player);
   }

   public int getShowNumber(Player player) {
      return player.getFinancingManager().getRiceiveNumber();
   }

   public boolean hasEffect(Player player, int showNumber) {
      return showNumber > 0;
   }
}
