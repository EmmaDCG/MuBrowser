package com.mu.game.model.map.event.req;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;

public abstract class MapEventRequirement {
   protected Map map = null;

   public MapEventRequirement(Map map) {
      this.map = map;
   }

   public abstract boolean math(Player var1);

   public void destroy() {
      this.map = null;
   }
}
