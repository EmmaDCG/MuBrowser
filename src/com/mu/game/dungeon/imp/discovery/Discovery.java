package com.mu.game.dungeon.imp.discovery;

import com.mu.game.dungeon.Dungeon;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.DungeonQuitPopup;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import java.awt.Point;

public class Discovery extends Dungeon {
   private DiscoveryInfo info = null;
   private int discoveyLevel = 0;

   public Discovery(int id, DiscoveryTemplate t, DiscoveryInfo info, int discoveyLevel) {
      super(id, t);
      this.info = info;
      this.discoveyLevel = discoveyLevel;
      this.timeLeft = this.timeLimit;
   }

   public void initMap() {
      DiscoveryMap map = new DiscoveryMap(this.info.getMapId(), this);
      map.setName(this.info.getMapName());
      map.setDefaultPoint(new Point(this.info.getX(), this.info.getY()));
      this.addMap(map);
   }

   public DiscoveryInfo getInfo() {
      return this.info;
   }

   public int getDiscoveyLevel() {
      return this.discoveyLevel;
   }

   public void exitForInitiative(Player player, boolean force) {
      if (!force && !this.isSuccess()) {
         DungeonQuitPopup pop = new DungeonQuitPopup(player.createPopupID());
         ShowPopup.open(player, pop);
      } else {
         super.exitForInitiative(player, force);
      }
   }

   public DungeonResult createSuccessPacket() {
      return null;
   }

   private DiscoveryMap getDiscoveryMap() {
      Map map = this.getFirstMap();
      return map == null ? null : (DiscoveryMap)map;
   }

   public void checkTime() {
      super.checkTime();
      DiscoveryMap dm = this.getDiscoveryMap();
      if (dm != null && this.getActiveTime() > 0) {
         dm.pushSchedule();
      }

   }

   public synchronized void destroy() {
      super.destroy();
   }
}
