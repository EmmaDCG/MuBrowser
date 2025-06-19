package com.mu.game.dungeon.imp.gangboss;

import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.model.drop.SystemDropManager;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;
import java.util.Iterator;

public class GangBossMonster extends DungeonMonster {
   public GangBossMonster(GangBossGroup md, GangBossMap map) {
      super(md, map);
      this.resetDropList(md.getDrops());
   }

   public GangBossMap getGangBossMap() {
      return (GangBossMap)this.getMap();
   }

   public void dropItem(Player player, int delay) {
      GangBossMap map = this.getGangBossMap();
      Iterator it = map.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         SystemDropManager.dropWhenMonsterBeKill(this, (Player)it.next(), delay, true);
      }

   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      this.getGangBossMap().bossBeKilled(this);
   }
}
