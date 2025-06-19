package com.mu.game.model.ui.dm.imp.other;

import com.mu.config.Global;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class TanxianMenu extends DynamicMenu {
   public TanxianMenu() {
      super(33);
   }

   public int getShowNumber(Player player) {
      return 0;
   }

   public boolean isShow(Player player) {
      return Global.isInterServiceServer() ? false : super.isShow(player);
   }

   public boolean hasEffect(Player player, int showNumber) {
      return false;
   }
}
