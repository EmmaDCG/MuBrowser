package com.mu.game.dungeon.map;

import com.mu.game.dungeon.Dungeon;
import com.mu.game.model.unit.player.Player;

public abstract class SingleDungeonMap extends DungeonMap {
   private Player owner;
   protected boolean isInited = false;

   public SingleDungeonMap(int referMapID, Dungeon d) {
      super(referMapID, d);
   }

   public void doEnterMapSpecil(Player player) {
      this.owner = player;
      if (!this.isInited) {
         this.init();
         this.isInited = true;
      }

      super.doEnterMapSpecil(player);
   }

   public Player getOwner() {
      return this.owner;
   }

   public Player removePlayer(Player player) {
      return super.removePlayer(player);
   }

   public synchronized void destroy() {
      if (!this.isDestroy) {
         this.owner = null;
         super.destroy();
      }
   }

   public abstract void init();
}
