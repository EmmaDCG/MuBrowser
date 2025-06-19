package com.mu.game.dungeon.imp.plot;

import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.model.map.BigMonsterGroup;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;

public class PlotMonster extends DungeonMonster {
   public PlotMonster(BigMonsterGroup md, PlotMap map) {
      super(md, map);
      this.setBossRank(md.getBossRank());
   }

   public PlotMap getPlotMap() {
      return (PlotMap)this.getMap();
   }

   public int decreaseHp(Creature attacker, AttackResult result) {
      return ((Plot)this.getPlotMap().getDungeon()).isComplete() ? 0 : super.decreaseHp(attacker, result);
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      this.getPlotMap().monsterBeKilled(this);
   }
}
