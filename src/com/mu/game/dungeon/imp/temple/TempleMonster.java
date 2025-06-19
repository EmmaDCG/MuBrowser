package com.mu.game.dungeon.imp.temple;

import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;

public class TempleMonster extends DungeonMonster {
   private int bossId = 0;
   private int header = -1;

   public TempleMonster(TempleMonsterGroup group, TempleMap map) {
      super(group, map);
      this.bossId = group.getBossId();
      this.header = group.getHeader();
   }

   public TempleMap getTempleMap() {
      return (TempleMap)this.getMap();
   }

   public int getBossId() {
      return this.bossId;
   }

   public int getHeader() {
      return this.header;
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      TempleMap map = this.getTempleMap();
      if (map.hasBoss(this.bossId)) {
         map.broadcastSchdule();
      }

   }

   public void revival() {
      super.revival();
      TempleMap map = this.getTempleMap();
      if (map.hasBoss(this.bossId)) {
         map.broadcastSchdule();
      }

   }
}
