package com.mu.game.model.ui.dm.imp.dungeon;

import com.mu.game.model.unit.player.Player;

public class TrialMenu extends DungeonMenu {
   public TrialMenu() {
      super(7, 3);
   }

   public boolean hasEffect(Player player, int showNumber) {
      return showNumber > 0;
   }
}
