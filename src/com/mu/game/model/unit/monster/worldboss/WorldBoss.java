package com.mu.game.model.unit.monster.worldboss;

import com.mu.config.BroadcastManager;
import com.mu.db.manager.PlayerDBManager;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.map.Map;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.monster.RefreshCanKillBoss;
import com.mu.io.game.packet.imp.monster.RefreshSingleBoss;
import com.mu.utils.concurrent.ThreadCachedPoolManager;

public class WorldBoss extends Monster {
   private int bossId = 0;

   public WorldBoss(long id, Map map, int bossId) {
      super(id, map);
      this.bossId = bossId;
      this.setBossRank(2);
   }

   public int getBossId() {
      return this.bossId;
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      Player player = null;
      if (attacker.getUnitType() == 1) {
         player = (Player)attacker;
      } else if (attacker.getUnitType() == 4) {
         player = ((Pet)attacker).getOwner();
      }

      if (player != null) {
         player.getTaskManager().onEventCheckCount(TargetType.CountType.KillWorldBoss);
         ActivityManager.bossBeKilled(this.bossId, player.getID());
         final long pId = player.getID();
         final String pName = player.getName();
         final int bId = this.bossId;
         ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
            public void run() {
               PlayerDBManager.insertKillBossRecord(bId, pId, pName);
            }
         });
      }

      RefreshCanKillBoss.brodcast();
      RefreshSingleBoss.broadcast(WorldBossManager.getBossData(this.bossId));
   }

   public void revival() {
      super.revival();
      BroadcastManager.broadcastBossRefresh(WorldBossManager.getBossData(this.bossId));
      RefreshCanKillBoss.brodcast();
      RefreshSingleBoss.broadcast(WorldBossManager.getBossData(this.bossId));
   }
}
