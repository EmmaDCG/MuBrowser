package com.mu.game.dungeon.imp.plot;

import com.mu.game.IDFactory;
import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.dungeon.MonsterDie;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.map.TranspointData;
import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.task.Task;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.tp.TransPoint;
import com.mu.io.game.packet.imp.map.AroundTransPoint;
import com.mu.io.game.packet.imp.player.hangset.StartHang;
import com.mu.io.game.packet.imp.task.TaskPushNext;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

public class PlotMap extends DungeonMap implements MonsterDie {
   private TransPoint tp = null;
   private int killNumber = 0;
   private int totalNumber = 0;
   private boolean checkDrop = false;

   public PlotMap(Plot d) {
      super(d.getPlotLevel().getMapId(), d);
      this.setCanTransmit(false);
      this.setDefaultPoint(new Point(d.getPlotLevel().getX(), d.getPlotLevel().getY()));
      this.createMonster();
   }

   private void createMonster() {
      ArrayList list = ((Plot)this.getDungeon()).getPlotLevel().getMonsterList();
      Iterator it = list.iterator();

      while(it.hasNext()) {
         PlotMonsterGroup group = (PlotMonsterGroup)it.next();
         int num = group.getNum();

         for(int i = 0; i < num; ++i) {
            PlotMonster monster = new PlotMonster(group, this);
            this.addMonster(monster);
         }
      }

      this.totalNumber = this.getMonsterSize();
      MapData md = MapConfig.getMapData(this.getID());
      it = md.getTransPointMap().values().iterator();
      if (it.hasNext()) {
         TranspointData td = (TranspointData)it.next();
         this.tp = new TransPoint(IDFactory.getTemporaryID(), this, td.getTargetMapID(), td.getX(), td.getY(), td.getTargetX(), td.getTargetY(), td.getName(), td.getWorldX(), td.getWorldY());
      }

   }

   public Player removePlayer(Player player) {
      try {
         PlayerTaskManager tm = player.getTaskManager();
         Task task = tm.getCurZJTask();
         task.forceComplete();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      super.removePlayer(player);
      ((Plot)this.getDungeon()).removePlayer(player);
      return player;
   }

   public void pushSchedule() {
   }

   private void pushLeave() {
      Iterator it = this.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         TaskPushNext.pushTaskContinue((Player)it.next());
      }

   }

   private void finishTask() {
      Iterator it = this.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         PlayerTaskManager tm = ((Player)it.next()).getTaskManager();
         Task task = tm.getCurZJTask();
         task.forceComplete();
      }

   }

   public void doEnterMapSpecil(final Player player) {
      super.doEnterMapSpecil(player);
      ((Plot)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
         public void run() {
            if (!player.getGameHang().isInHanging()) {
               StartHang.start(player);
            }

         }
      }, 2500L));
   }

   protected void doWork(long now) {
      super.doWork(now);
      if (this.checkDrop && this.getDropItemSize() == 0) {
         this.checkDrop = false;
         ((Plot)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
            public void run() {
               PlotMap.this.pushLeave();
            }
         }, 500L));
      }

   }

   public void monsterBeKilled(PlotMonster monster) {
      monster.setShouldDestroy(true);
      ++this.killNumber;
      if (this.killNumber >= this.totalNumber) {
         this.finishTask();
         this.setCanTransmit(true);
         this.addTranspoint(this.tp);
         AroundTransPoint ap = new AroundTransPoint(this.tp);
         this.broadcastPacket(ap);
         ap.destroy();
         ap = null;
         this.checkDrop = true;
         ((Plot)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
            public void run() {
               PlotMap.this.pushLeave();
            }
         }, 30000L));
      }

   }

   @Override
   public void monsterBeKilled(DungeonMonster var1) {

   }
}
