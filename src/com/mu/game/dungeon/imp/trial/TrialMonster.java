package com.mu.game.dungeon.imp.trial;

import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;

public class TrialMonster extends DungeonMonster {
   private boolean isBroadcast;

   public TrialMonster(TrialMonsterGroup md, TrialMap map) {
      super(md, map);
      this.isBroadcast = md.isBroadCast();
      this.setBossRank(2);
   }

   public TrialMap getTrialMap() {
      return (TrialMap)this.getMap();
   }

   public boolean isBroadcast() {
      return this.isBroadcast;
   }

   public int decreaseHp(Creature attacker, AttackResult result) {
      return ((Trial)this.getTrialMap().getDungeon()).isComplete() ? 0 : super.decreaseHp(attacker, result);
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      this.getTrialMap().monsterBeKilled(this);
   }
}
