package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class SevenDayMenu extends DynamicMenu {
   public SevenDayMenu() {
      super(31);
   }

   public int getShowNumber(Player player) {
      return player.getSevenManager().getRemainCount();
   }

   public boolean isShow(Player player) {
      boolean b = super.isShow(player);
      return b && !player.getSevenManager().isShowInMap() ? false : b;
   }

   public boolean hasEffect(Player player, int showNumber) {
      return showNumber > 0;
   }
}
