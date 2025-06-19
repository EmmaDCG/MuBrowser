package com.mu.game.model.unit.action.imp;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.unit.player.Player;

public class EnterPlotAcion extends XmlAction {
   private int plotId;

   public EnterPlotAcion(int id) {
      super(id);
   }

   public void doAction(Player player) {
      DungeonManager.createAndEnterDungeon(player, 8, this.plotId);
   }

   public void destroy() {
   }

   public void initAction(String value) {
      this.plotId = Integer.parseInt(value);
   }
}
