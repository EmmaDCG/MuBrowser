package com.mu.game.dungeon.imp.devil;

import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.dungeon.MonsterDie;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dungeon.DungeonInfoUpdate;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.io.game.packet.imp.monster.AroundMonster;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.util.ArrayList;
import java.util.Iterator;

public class DevilSquareMap extends DungeonMap implements MonsterDie {
   private static final int MaxBatch = 4;
   private int PerBatchTime = 150;
   private int currentBatch = 0;
   private boolean hasGuideInspire = false;

   public DevilSquareMap(DevilSquare d) {
      super(((DevilSquareTemplate)d.getTemplate()).getDefaultMapID(), d);
      this.PerBatchTime = d.getTimeLimit() / 4;
   }

   public void start() {
      this.refreshMonster();
   }

   public int getPercent(DevilSquareRank rank) {
      int base = rank.getRank() * 250;
      DevilSquareTemplate template = (DevilSquareTemplate)((DevilSquare)this.getDungeon()).getTemplate();
      if (rank.getRank() == template.getMaxRank()) {
         return 1000;
      } else {
         int curMax = rank.getMaxKill() - rank.getMinKill() + 1;
         int curSchdule = rank.getMaxKill() - ((DevilSquare)this.getDungeon()).getKillNum() + 1;
         base += 250 - (int)((float)curSchdule * 1.0F / (float)curMax * 1.0F * 250.0F);
         return base;
      }
   }

   public void pushPlayerSchedule() {
      try {
         DevilSquareTemplate template = (DevilSquareTemplate)((DevilSquare)this.getDungeon()).getTemplate();
         DevilSquareRank rank = template.getRank(((DevilSquare)this.getDungeon()).getKillNum());
         DungeonInfoUpdate du = new DungeonInfoUpdate();
         du.writeByte(template.getTemplateID());
         du.writeShort(((DevilSquare)this.getDungeon()).getKillNum());
         du.writeDouble((double)((DevilSquare)this.getDungeon()).getTotalExp());
         du.writeByte(this.currentBatch);
         du.writeByte(4);
         du.writeUTF(rank.getTarget());
         du.writeByte(rank.getRank());
         du.writeShort(this.getPercent(rank));
         du.writeShort(1000);
         du.writeUTF(rank.getExpAdditionDes());
         du.writeUTF(rank.getLevelUpStr().replace("%s%", String.valueOf(rank.getMaxKill() - ((DevilSquare)this.getDungeon()).getKillNum() + 1)));
         du.writeInt(((DevilSquare)this.getDungeon()).getActiveTime() < 0 ? 0 : ((DevilSquare)this.getDungeon()).getActiveTime());
         this.broadcastPacket(du);
         du.destroy();
         du = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void pushSchedule() {
      this.pushPlayerSchedule();
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
      ru = null;
      list.clear();
      list = null;
   }

   private void refreshMonster() {
      this.clearMonster();
      ++this.currentBatch;
      DevilSquareLevel dl = ((DevilSquare)this.getDungeon()).getSquareLevel();
      if (dl != null && this.currentBatch <= 4) {
         ArrayList list = dl.getMonsterGroup(this.currentBatch);
         ArrayList monsterList = new ArrayList();
         Iterator var5 = list.iterator();

         DevilMonsterGroup group;
         while(var5.hasNext()) {
            group = (DevilMonsterGroup)var5.next();
            int num = group.getNum();

            for(int i = 0; i < num; ++i) {
               DevilSquareMonster monster = new DevilSquareMonster(group, this);
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
            ((DevilSquare)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
               public void run() {
                  DevilSquareMap.this.refreshMonster();
               }
            }, (long)(this.PerBatchTime * 1000)));
         }

      }
   }

   public void playerDie(Player player, Creature attacker) {
      DungeonResult.faild(player, ((DevilSquareTemplate)((DevilSquare)this.getDungeon()).getTemplate()).getTemplateID());
   }

   public void doSuccess() {
      ((DevilSquare)this.getDungeon()).cancelUniqueFuture();
      this.clearMonster();
      ((DevilSquare)this.getDungeon()).doSuccess();
      Iterator it = this.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();

         try {
            DevilSquareRank rank = ((DevilSquareTemplate)((DevilSquare)this.getDungeon()).getTemplate()).getRank(((DevilSquare)this.getDungeon()).getKillNum());
            PlayerTaskManager tm = player.getTaskManager();
            if (tm != null) {
               tm.onEventCheckSpecify(TargetType.SpecifyType.FB_Appraise, ((DevilSquareTemplate)((DevilSquare)this.getDungeon()).getTemplate()).getTemplateID(), rank.getRank());
            }

            ((DevilSquare)this.getDungeon()).doReceive(player);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }

   public void doEnterMapSpecil(Player player) {
      super.doEnterMapSpecil(player);
      if (!this.hasGuideInspire) {
         this.hasGuideInspire = true;
         Iterator it = this.playerMap.values().iterator();

         while(it.hasNext()) {
            ArrowGuideManager.pushArrow((Player)it.next(), 23, (String)null);
         }
      }

   }

   public void monsterBeKilled(DevilSquareMonster monster) {
      if (!((DevilSquare)this.getDungeon()).isSuccess()) {
         ((DevilSquare)this.getDungeon()).addKillNum();
      }

      if (monster.getBatch() != this.currentBatch) {
         monster.setShouldDestroy(true);
         monster.setRevivalTime(-1L);
      }

   }

   @Override
   public void monsterBeKilled(DungeonMonster var1) {

   }
}
