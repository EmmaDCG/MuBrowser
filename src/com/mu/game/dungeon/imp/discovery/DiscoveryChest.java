package com.mu.game.dungeon.imp.discovery;

import com.mu.game.IDFactory;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.material.Material;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.material.CollectResult;

public class DiscoveryChest extends Material {
   private ChestInfo info = null;

   public DiscoveryChest(Map map, ChestInfo info) {
      super(IDFactory.getTemporaryID(), map, info.getTemplateId(), info.getName(), info.getModelId());
      this.info = info;
      this.setX(info.getX());
      this.setY(info.getY());
      this.setCanDisappear(false);
   }

   public DiscoveryMap getDiscoveryMap() {
      Map map = this.getMap();
      return map != null ? (DiscoveryMap)map : null;
   }

   public boolean canClick(Player player) {
      return !this.getDiscoveryMap().isChestItemBeReward();
   }

   public synchronized void countdownEnd(Player player) {
      this.gathers.remove(player.getID());
      CollectResult.sendResult(player, this.getTemplateID(), false);
      this.getDiscoveryMap().chestBeCollect(this, player);
   }
}
