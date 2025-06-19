package com.mu.game.dungeon.imp.discovery;

import com.mu.game.model.map.BigMonsterGroup;

public class DiscoveyMonsterGroup extends BigMonsterGroup {
   private int dunType = 0;
   private int discoveryLevel = 0;

   public int getDunType() {
      return this.dunType;
   }

   public void setDunType(int dunType) {
      this.dunType = dunType;
   }

   public int getDiscoveryLevel() {
      return this.discoveryLevel;
   }

   public void setDiscoveryLevel(int discoveryLevel) {
      this.discoveryLevel = discoveryLevel;
   }
}
