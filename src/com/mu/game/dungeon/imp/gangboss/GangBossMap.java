package com.mu.game.dungeon.imp.gangboss;

import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.unit.Unit;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.utils.concurrent.ThreadFixedPoolManager;

public class GangBossMap extends DungeonMap {
   private GangBossMonster boss = null;
   private boolean timeIsOver = false;

   public GangBossMap(int referMapID, GangBoss d) {
      super(referMapID, d);
      this.setName(((GangBoss)this.getDungeon()).getBossGroup().getName());
   }

   public void init() {
      GangBossGroup group = ((GangBoss)this.getDungeon()).getBossGroup();
      this.boss = new GangBossMonster(group, this);
      this.addMonster(this.boss);
   }

   public void timeIsOver() {
      GangBossManager.getManager().removeGangBoss(((GangBoss)this.getDungeon()).getGangId());
      ((GangBoss)this.getDungeon()).setComplete(true);
      this.boss.setShouldDestroy(true);
      RemoveUnit rm = new RemoveUnit(new Unit[]{this.boss});
      this.broadcastPacket(rm);
      DungeonResult dr = DungeonResult.getFailResult(10);
      this.broadcastPacket(dr);
      dr.destroy();
      rm.destroy();
      ((GangBoss)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
         public void run() {
            ((GangBoss)GangBossMap.this.getDungeon()).destroy();
         }
      }, 30000L));
   }

   public void setTimeIsOver(boolean timeIsOver) {
      this.timeIsOver = timeIsOver;
   }

   public void doSuccess() {
      ((GangBoss)this.getDungeon()).setComplete(true);
      ((GangBoss)this.getDungeon()).setSuccess(true);
      ((GangBoss)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
         public void run() {
            ((GangBoss)GangBossMap.this.getDungeon()).destroy();
         }
      }, 60000L));
      GangBossManager.getManager().removeGangBoss(((GangBoss)this.getDungeon()).getGangId());
   }

   protected void doWork(long now) {
      super.doWork(now);
      GangBoss gb = (GangBoss)this.getDungeon();
      if (gb != null && !gb.isComplete() && this.timeIsOver) {
         this.timeIsOver();
      }

   }

   public void bossBeKilled(GangBossMonster monster) {
      this.boss.setShouldDestroy(true);
      this.doSuccess();
   }

   public boolean canLeaveGang() {
      return false;
   }
}
