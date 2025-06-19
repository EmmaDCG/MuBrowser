package com.mu.game.model.unit.action.imp;

import com.mu.game.dungeon.Dungeon;
import com.mu.game.dungeon.DungeonPlayerInfo;
import com.mu.game.model.unit.action.Action;
import com.mu.game.model.unit.player.Player;

public class CreateInspireBuffAction implements Action {
   private Dungeon dungeon = null;

   public CreateInspireBuffAction(Dungeon dungeon) {
      this.dungeon = dungeon;
   }

   public void doAction(Player player) {
      DungeonPlayerInfo info = this.dungeon.getDungeonPlayerInfo(player.getID());
      if (info != null) {
         ;
      }
   }

   public void destroy() {
      this.dungeon = null;
   }
}
