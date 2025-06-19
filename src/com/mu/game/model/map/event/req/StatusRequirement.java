package com.mu.game.model.map.event.req;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;

public class StatusRequirement extends MapEventRequirement {
   private int state = 0;

   public StatusRequirement(Map map, int state) {
      super(map);
      this.state = state;
   }

   public boolean math(Player player) {
      return false;
   }
}
