package com.mu.game.model.ui.dm.imp.dungeon;

import com.mu.config.Global;
import com.mu.game.model.unit.player.Player;

public class TempleMenu extends DungeonMenu {
   public TempleMenu() {
      super(8, 4);
   }

   public boolean isShow(Player player) {
      return Global.isInterServiceServer() ? false : super.isShow(player);
   }

   public boolean hasEffect(Player player, int showNumber) {
      return showNumber > 0;
   }
}
