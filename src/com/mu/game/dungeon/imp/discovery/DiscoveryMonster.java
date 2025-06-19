package com.mu.game.dungeon.imp.discovery;

import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;

public class DiscoveryMonster extends DungeonMonster {
   public DiscoveryMonster(DiscoveyMonsterGroup md, DiscoveryMap map) {
      super(md, map);
      this.setRevivalTime(-1L);
      this.setBossRank(md.getBossRank());
   }

   private DiscoveryMap getDiscoveryMap() {
      Map map = this.getMap();
      return map != null ? (DiscoveryMap)map : null;
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      DiscoveryMap map = this.getDiscoveryMap();
      map.monsterBeKilled(this);
   }
}
