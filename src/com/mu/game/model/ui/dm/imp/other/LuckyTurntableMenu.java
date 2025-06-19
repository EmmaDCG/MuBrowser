package com.mu.game.model.ui.dm.imp.other;

import com.mu.game.model.luckyTurnTable.LuckyTurnTableManager;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public class LuckyTurntableMenu extends DynamicMenu {
   public LuckyTurntableMenu() {
      super(35);
   }

   public int getShowNumber(Player player) {
      return LuckyTurnTableManager.canTurn(player) == 1 ? 1 : 0;
   }

   public boolean isShow(Player player) {
      if (super.isShow(player)) {
         return LuckyTurnTableManager.isOpen() && player.getTurnTableCount() < LuckyTurnTableManager.getMaxCount();
      } else {
         return false;
      }
   }

   public boolean hasEffect(Player player, int showNumber) {
      return showNumber > 0;
   }
}
