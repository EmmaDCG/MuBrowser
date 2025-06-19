package com.mu.game.model.ui.dm.imp.other;

import com.mu.config.Global;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class LuolanMenu extends DynamicMenu {
   public LuolanMenu() {
      super(22);
   }

   public int getShowNumber(Player player) {
      return 0;
   }

   public boolean isShow(Player player) {
      return !Global.isInterServiceServer();
   }

   public boolean hasEffect(Player player, int showNumber) {
      if (!FunctionOpenManager.isOpen(player, 14)) {
         return false;
      } else {
         return DungeonManager.getLuolanManager().getLuoLan() != null;
      }
   }
}
