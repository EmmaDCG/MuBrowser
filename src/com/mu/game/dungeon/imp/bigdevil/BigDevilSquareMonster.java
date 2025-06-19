package com.mu.game.dungeon.imp.bigdevil;

import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.dungeon.DungeonPlayerInfo;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;

public class BigDevilSquareMonster extends DungeonMonster {
   private int batch;

   public BigDevilSquareMonster(BigDevilMonsterGroup md, BigDevilSquareMap map) {
      super(md, map);
      this.batch = md.getBatch();
   }

   public int getBatch() {
      return this.batch;
   }

   public BigDevilSquareMap getDevilSquareMap() {
      return (BigDevilSquareMap)this.getMap();
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      this.getDevilSquareMap().monsterBeKilled(this);
   }

   public long addExpToPlayer(Player player) {
      long exp = super.addExpToPlayer(player);
      DungeonPlayerInfo info = ((BigDevilSquare)this.getDevilSquareMap().getDungeon()).getDungeonPlayerInfo(player.getID());
      info.addKillNum();
      info.addExp(exp);
      return exp;
   }
}
