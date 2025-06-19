package com.mu.game.dungeon.imp.devil;

import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;

public class DevilSquareMonster extends DungeonMonster {
   private int batch;

   public DevilSquareMonster(DevilMonsterGroup md, DevilSquareMap map) {
      super(md, map);
      this.batch = md.getBatch();
   }

   public int getBatch() {
      return this.batch;
   }

   public DevilSquareMap getDevilSquareMap() {
      return (DevilSquareMap)this.getMap();
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      this.getDevilSquareMap().monsterBeKilled(this);
   }

   public long addExpToPlayer(Player player) {
      long exp = super.addExpToPlayer(player);
      ((DevilSquare)this.getDevilSquareMap().getDungeon()).addTotalExp(exp);
      return exp;
   }
}
