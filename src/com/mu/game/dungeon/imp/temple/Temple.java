package com.mu.game.dungeon.imp.temple;

import com.mu.game.dungeon.Dungeon;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.DungeonQuitPopup;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;

public class Temple extends Dungeon {
   private TempleLevel tl;

   public Temple(int id, TempleTemplate t, TempleLevel level) {
      super(id, t);
      this.tl = level;
      this.timeLimit = 1073741823;
      this.timeLeft = this.timeLimit;
   }

   public void initMap() {
      TempleMap tm = new TempleMap(this);
      this.addMap(tm);
      tm.init();
   }

   public TempleLevel getTempleLevel() {
      return this.tl;
   }

   public void exitForInitiative(Player player, boolean force) {
      if (!force) {
         DungeonQuitPopup pop = new DungeonQuitPopup(player.createPopupID());
         ShowPopup.open(player, pop);
      } else {
         super.exitForInitiative(player, force);
      }
   }

   public TempleMap getTempleMap() {
      Map map = this.getFirstMap();
      return map != null ? (TempleMap)map : null;
   }

   public void checkTime() {
   }

   public void doSuccess() {
   }

   public DungeonResult createSuccessPacket() {
      return null;
   }
}
