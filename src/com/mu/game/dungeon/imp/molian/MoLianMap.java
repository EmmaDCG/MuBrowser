package com.mu.game.dungeon.imp.molian;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.dungeon.DungeonPlayerInfo;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.unit.player.Player;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class MoLianMap extends DungeonMap {
   private long lastCheckPlayerTime = System.currentTimeMillis();

   public MoLianMap(int referMapID, MoLian d) {
      super(referMapID, d);
   }

   public void initMonster() {
      MoLianleLevel ml = ((MoLian)this.getDungeon()).getMolianLevel();
      Iterator var3 = ml.getMonsterList().iterator();

      while(var3.hasNext()) {
         MoLianMonsterGroup group = (MoLianMonsterGroup)var3.next();
         int num = group.getNum();

         for(int i = 0; i < num; ++i) {
            MoLianMonster monster = new MoLianMonster(group, this);
            this.addMonster(monster);
         }
      }

   }

   public void checkPlayer(DungeonPlayerInfo info) {
      Player player = this.getPlayer(info.getRid());
      if (info.getCostTime() >= ((MoLian)this.getDungeon()).getPlayerTimeLimit()) {
         if (player != null) {
            if (!player.isDestroy()) {
               ((MoLian)this.getDungeon()).exitForInitiative(player, true);
            } else {
               this.removePlayer(player);
               ((MoLian)this.getDungeon()).removePlayer(player);
            }
         }

         DungeonManager.removeInterruptInfo(info.getRid());
      }

   }

   protected void doWork(long now) {
      super.doWork(now);
      if (now - this.lastCheckPlayerTime >= 1000L) {
         try {
            MoLian molian = (MoLian)this.getDungeon();
            if (molian != null && !molian.isDestroy()) {
               ConcurrentHashMap infoMap = molian.getPlayerInfoMap();
               if (infoMap != null) {
                  Iterator it = infoMap.values().iterator();

                  while(it.hasNext()) {
                     DungeonPlayerInfo info = (DungeonPlayerInfo)it.next();
                     info.addCostTime(1);
                     this.checkPlayer(info);
                  }
               }
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         this.lastCheckPlayerTime = now;
      }

   }

   public boolean canLeaveGang() {
      return false;
   }
}
