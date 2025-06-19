package com.mu.game.dungeon.imp.bigdevil;

import com.mu.executor.imp.dun.UpdateBigDevilTopExecutor;
import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.dungeon.DungeonPlayerInfo;
import com.mu.game.dungeon.MonsterDie;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dungeon.DungeonInfoUpdate;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.io.game.packet.imp.monster.AroundMonster;
import com.mu.io.game.packet.imp.sys.CenterMessage;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.util.ArrayList;
import java.util.Iterator;

public class BigDevilSquareMap extends DungeonMap implements MonsterDie {
   private static final int MaxBatch = 4;
   private int PerBatchTime = 150;
   private int currentBatch = 0;

   public BigDevilSquareMap(BigDevilSquare d) {
      super(((BigDevilSquareTemplate)d.getTemplate()).getDefaultMapID(), d);
      this.PerBatchTime = d.getTimeLimit() / 4;
      this.setCanPk(false);
   }

   public void start() {
      try {
         CenterMessage cm = new CenterMessage();
         cm.writeUTF(((BigDevilSquareTemplate)((BigDevilSquare)this.getDungeon()).getTemplate()).getStartStr());
         this.broadcastPacket(cm);
         cm.destroy();
         cm = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      this.refreshMonster();
   }

   public void doEnterMapSpecil(Player player) {
      super.doEnterMapSpecil(player);
      int tmp = ((BigDevilSquare)this.getDungeon()).getStartTimeLeft();
      if (tmp > 0) {
         tmp = tmp % 60 == 0 ? tmp / 60 : tmp / 60 + 1;

         try {
            CenterMessage cm = new CenterMessage();
            cm.writeUTF(((BigDevilSquareTemplate)((BigDevilSquare)this.getDungeon()).getTemplate()).getEnterMsg().replace("%s%", String.valueOf(tmp)));
            player.writePacket(cm);
            cm.destroy();
            cm = null;
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   public void pushSchedule() {
      Iterator it = this.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         try {
            Player player = (Player)it.next();
            DungeonPlayerInfo info = ((BigDevilSquare)this.getDungeon()).getDungeonPlayerInfo(player.getID());
            DungeonInfoUpdate du = new DungeonInfoUpdate();
            du.writeByte(((BigDevilSquareTemplate)((BigDevilSquare)this.getDungeon()).getTemplate()).getTemplateID());
            du.writeShort(info.getKillNum());
            du.writeDouble((double)info.getExp());
            du.writeByte(this.currentBatch);
            du.writeByte(4);
            du.writeInt(((BigDevilSquare)this.getDungeon()).getActiveTime() < 0 ? 0 : ((BigDevilSquare)this.getDungeon()).getActiveTime());
            player.writePacket(du);
            du.destroy();
            du = null;
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }

   private void clearMonster() {
      ArrayList list = new ArrayList();
      Iterator ru = this.getMonsterMap().values().iterator();

      while(ru.hasNext()) {
         Monster monster = (Monster)ru.next();
         list.add(monster);
         monster.setShouldDestroy(true);
      }

      RemoveUnit ru2 = new RemoveUnit(list);
      this.broadcastPacket(ru2);
      ru2.destroy();
      ru2 = null;
      list.clear();
      list = null;
   }

   private void refreshMonster() {
      this.clearMonster();
      ++this.currentBatch;
      BigDevilSquareLevel dl = ((BigDevilSquare)this.getDungeon()).getSquareLevel();
      if (dl != null && this.currentBatch <= 4) {
         ArrayList list = dl.getMonsterGroup(this.currentBatch);
         ArrayList monsterList = new ArrayList();
         Iterator var5 = list.iterator();

         BigDevilMonsterGroup group;
         while(var5.hasNext()) {
            group = (BigDevilMonsterGroup)var5.next();
            int num = group.getNum();

            for(int i = 0; i < num; ++i) {
               BigDevilSquareMonster monster = new BigDevilSquareMonster(group, this);
               monsterList.add(monster);
               this.addMonster(monster);
            }
         }

         AroundMonster am = new AroundMonster(monsterList);
         this.broadcastPacket(am);
         am.destroy();
         group = null;
         monsterList.clear();
         monsterList = null;
         if (this.currentBatch < 4) {
            ((BigDevilSquare)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
               public void run() {
                  BigDevilSquareMap.this.refreshMonster();
               }
            }, (long)(this.PerBatchTime * 1000)));
         }

      }
   }

   public void playerDie(Player player, Creature attacker) {
      super.playerDie(player, attacker);
   }

   public void doSuccess() {
      ((BigDevilSquare)this.getDungeon()).cancelUniqueFuture();
      this.clearMonster();
      ((BigDevilSquare)this.getDungeon()).doSuccess();
      long now = System.currentTimeMillis();
      Iterator it = this.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         DungeonPlayerInfo info = ((BigDevilSquare)this.getDungeon()).getDungeonPlayerInfo(player.getID());
         if (info != null) {
            UpdateBigDevilTopExecutor.updateTop(player, info.getExp(), ((BigDevilSquare)this.getDungeon()).getSquareLevel().getLevel(), now);
         }
      }

   }

   public void monsterBeKilled(BigDevilSquareMonster monster) {
      if (monster.getBatch() != this.currentBatch) {
         monster.setShouldDestroy(true);
         monster.setRevivalTime(-1L);
      }

   }

   @Override
   public void monsterBeKilled(DungeonMonster var1) {

   }
}
