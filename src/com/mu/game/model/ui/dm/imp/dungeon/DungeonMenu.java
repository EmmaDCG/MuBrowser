package com.mu.game.model.ui.dm.imp.dungeon;

import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;

public abstract class DungeonMenu extends DynamicMenu {
   private int dungeonId;

   public DungeonMenu(int menuId, int dungeonId) {
      super(menuId);
      this.dungeonId = dungeonId;
   }

   public int getShowNumber(Player player) {
      DungeonTemplate template = DungeonTemplateFactory.getTemplate(this.dungeonId);
      return template != null ? template.getPlayerLeftTimes(player, 0) : 0;
   }

   public int getDungeonId() {
      return this.dungeonId;
   }
}
