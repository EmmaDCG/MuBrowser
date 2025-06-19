package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class ExTargetMenu extends DynamicMenu {
   public ExTargetMenu() {
      super(24);
   }

   public int getShowNumber(Player player) {
      return player.getExtargetManager().getShowNumber();
   }

   public boolean isShow(Player player) {
      return player.getExtargetManager().isAllReceived() ? false : super.isShow(player);
   }

   public boolean hasEffect(Player player, int showNumber) {
      return showNumber > 0;
   }
}
