package com.mu.game.model.map.event.req;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;

public class TaskAcceptRequirement extends MapEventRequirement {
   private int taskID = -1;

   public TaskAcceptRequirement(Map map, int taskID) {
      super(map);
      this.taskID = taskID;
   }

   public boolean math(Player player) {
      return false;
   }
}
