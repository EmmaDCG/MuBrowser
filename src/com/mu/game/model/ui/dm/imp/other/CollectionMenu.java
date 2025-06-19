package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class CollectionMenu extends DynamicMenu {
   public CollectionMenu() {
      super(19);
   }

   public boolean isShow(Player player) {
      return false;
   }

   public int getShowNumber(Player player) {
      return 0;
   }

   public boolean hasEffect(Player player, int showNumber) {
      return false;
   }
}
