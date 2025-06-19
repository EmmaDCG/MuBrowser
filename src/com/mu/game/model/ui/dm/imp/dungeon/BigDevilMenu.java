package com.mu.game.model.ui.dm.imp.dungeon;

import com.mu.config.Global;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.dungeon.imp.bigdevil.BigDevilSquareTemplate;
import com.mu.game.model.unit.player.Player;

public class BigDevilMenu extends DungeonMenu {
   public BigDevilMenu() {
      super(13, 6);
   }

   public boolean isShow(Player player) {
      return Global.isInterServiceServer() ? false : super.isShow(player);
   }

   public boolean hasEffect(Player player, int showNumber) {
      BigDevilSquareTemplate template = (BigDevilSquareTemplate)DungeonTemplateFactory.getTemplate(6);
      return template.getBigDevilManager().isOpen() && !template.getBigDevilManager().isBegin();
   }
}
