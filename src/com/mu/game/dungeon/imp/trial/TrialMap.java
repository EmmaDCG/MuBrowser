package com.mu.game.dungeon.imp.trial;

import com.mu.config.BroadcastManager;
import com.mu.executor.Executor;
import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.dungeon.MonsterDie;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.trial.TrialConfigs;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.dungeon.DungeonInfoUpdate;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.monster.AroundMonster;
import com.mu.io.game.packet.imp.pkModel.ChangePkView;
import com.mu.io.game.packet.imp.player.ChangePlayerWarCommentIcon;
import com.mu.io.game.packet.imp.player.PushWarCommentText;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.awt.Point;
import java.util.Iterator;
import java.util.List;

public class TrialMap extends DungeonMap implements MonsterDie {
   private TrialMonster boss = null;
   private boolean pushArrow = false;

   public TrialMap(Trial d) {
      super(((TrialTemplate)d.getTemplate()).getDefaultMapID(), d);
      this.setDefaultPoint(new Point(((TrialTemplate)d.getTemplate()).getBornX(), ((TrialTemplate)d.getTemplate()).getBornY()));
      ((Trial)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
         public void run() {
            TrialMap.this.createBoss();
         }
      }, 5000L));
   }

   private void createBoss() {
      TrialMonsterGroup group = ((TrialTemplate)((Trial)this.getDungeon()).getTemplate()).getMonsterGroup(((Trial)this.getDungeon()).getTrialLevel());
      this.boss = new TrialMonster(group, this);
      this.addMonster(this.boss);
      this.boss.setRevivalTime(-1L);
      AroundMonster am = new AroundMonster(this.boss);
      this.broadcastPacket(am);
      am.destroy();
      am = null;
   }

   public void doFaild() {
      ((Trial)this.getDungeon()).setComplete(true);
      if (this.boss != null) {
         this.boss.setAI(0);
         this.boss.setCanBeAttacked(false);
         ChangePkView cv = new ChangePkView(this.boss, false);
         this.broadcastPacket(cv);
         cv.destroy();
         cv = null;
      }

      Iterator it = this.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         player.getDunLogsManager().finishDungeon(((TrialTemplate)((Trial)this.getDungeon()).getTemplate()).getTemplateID(), 0);
         UpdateMenu.updateDungeonMenu(player, ((TrialTemplate)((Trial)this.getDungeon()).getTemplate()).getTemplateID());
         DungeonResult.faild(player, ((TrialTemplate)((Trial)this.getDungeon()).getTemplate()).getTemplateID());
      }

      ((Trial)this.getDungeon()).readyToQuitForComplete();
   }

   public void pushSchedule() {
      TrialConfigs config = TrialConfigs.getConfig(((Trial)this.getDungeon()).getTrialLevel());
      TrialLevel tl = ((TrialTemplate)((Trial)this.getDungeon()).getTemplate()).getTrialLevel(((Trial)this.getDungeon()).getTrialLevel());
      if (config != null && tl != null) {
         try {
            DungeonInfoUpdate du = new DungeonInfoUpdate();
            du.writeByte(((TrialTemplate)((Trial)this.getDungeon()).getTemplate()).getTemplateID());
            du.writeUTF(config.getName());
            du.writeUTF(tl.getTargetDes());
            du.writeInt(((Trial)this.getDungeon()).getTimeLimit());
            du.writeInt(((Trial)this.getDungeon()).getTimeLimit() - ((Trial)this.getDungeon()).getTimeCost());
            this.broadcastPacket(du);
            du.destroy();
            du = null;
            if (!this.pushArrow) {
               this.pushArrow = true;
               Iterator it = this.playerMap.values().iterator();

               while(it.hasNext()) {
                  ArrowGuideManager.pushArrow((Player)it.next(), 19, (String)null);
               }
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      }
   }

   public void playerDie(Player player, Creature attacker) {
      this.doFaild();
   }

   public void doSuccess() {
      Iterator it = this.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         this.trialSuccess(player);
         if (this.boss.isBroadcast()) {
            BroadcastManager.broadcastTrial(player, this.boss.getName(), ((TrialTemplate)((Trial)this.getDungeon()).getTemplate()).getBroadcastContent(), ((TrialTemplate)((Trial)this.getDungeon()).getTemplate()).getLinkStr());
         }
      }

      ((Trial)this.getDungeon()).doSuccess();
      ((Trial)this.getDungeon()).readyToQuitForComplete();
   }

   public void trialSuccess(Player player) {
      player.setWarComment(player.getWarComment() + 1);
      player.setWarCommentTime(System.currentTimeMillis());
      WriteOnlyPacket packet = Executor.SaveWarComment.toPacket(player.getID(), player.getWarComment(), System.currentTimeMillis());
      player.writePacket(packet);
      packet.destroy();
      packet = null;
      TrialConfigs config = TrialConfigs.getConfig(player.getWarComment());
      if (config != null) {
         player.getBuffManager().createAndStartBuff(player, config.getBuffId(), config.getBuffLevel(), true, 0L, (List)null);
         PushWarCommentText.pushText(player);
         ChangePlayerWarCommentIcon.change(player);
      }

      player.getTaskManager().onEventCheckValue(TargetType.ValueType.ZhanPing);
   }

   public void monsterBeKilled(TrialMonster monster) {
      if (!((Trial)this.getDungeon()).isComplete()) {
         this.doSuccess();
      }

   }

   @Override
   public void monsterBeKilled(DungeonMonster var1) {

   }
}
