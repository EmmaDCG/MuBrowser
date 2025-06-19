package com.mu.game.model.ui.dm.imp.dungeon;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.dun.DunLogs;

public class DevilSqureMenu extends DungeonMenu {
   public DevilSqureMenu() {
      super(2, 2);
   }

   public boolean hasEffect(Player player, int showNumber) {
      DunLogs logs = player.getDunLogsManager().getLog(2);
      if (logs == null) {
         return true;
      } else {
         return logs.notReceivd() || showNumber > 0;
      }
   }
}
