package com.mu.game.dungeon.imp.bloodcastle;

import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;

public class BloodCastleMonster extends DungeonMonster {
   private int batch;

   public BloodCastleMonster(BloodCastleMonsterGroup group, BloodCastleMap map) {
      super(group, map);
      this.batch = group.getBatch();
      this.setBossRank(group.getBossRank());
   }

   public BloodCastleMap getBloodCastleMap() {
      return (BloodCastleMap)this.getMap();
   }

   public int getBatch() {
      return this.batch;
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      this.getBloodCastleMap().monsterBeKilled(this);
   }
}
