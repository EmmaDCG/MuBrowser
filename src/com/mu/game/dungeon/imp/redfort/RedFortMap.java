package com.mu.game.dungeon.imp.redfort;

import com.mu.config.MessageText;
import com.mu.game.dungeon.MultiReceive;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.mail.SendMailTask;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatList2Client;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.player.AroundPlayer;
import com.mu.io.game.packet.imp.player.ExternalChange;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import com.mu.io.game.packet.imp.sys.CenterMessage;
import com.mu.utils.Rnd;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class RedFortMap extends DungeonMap implements MultiReceive {
   private int bornRadius = 5000;
   private int floor;
   private boolean isStart = false;
   private int timeCost = 0;
   private ScheduledFuture checkTimeFuture = null;
   private boolean isToNext = false;

   public RedFortMap(int referMapID, RedFort d, int floor) {
      super(referMapID, d);
      this.floor = floor;
      this.setCanChangePkMode(false);
      this.setPkPunishment(false);
      this.setCanPk(true);
   }

   public Point getBornPoint() {
      RedFortFloor rf = ((RedFortTemplate)((RedFort)this.getDungeon()).getTemplate()).getFloor(this.floor);
      if (rf == null) {
         return new Point();
      } else {
         Point defaultPoint = rf.getBornPoints()[Rnd.get(rf.getBornPoints().length)];
         int x = Rnd.get(defaultPoint.x - this.bornRadius, defaultPoint.x + this.bornRadius);
         int y = Rnd.get(defaultPoint.y - this.bornRadius, defaultPoint.y + this.bornRadius);
         return this.isBlocked(x, y) ? defaultPoint : new Point(x, y);
      }
   }

   public boolean isPkPunishment() {
      return false;
   }

   public RedFortFloor getRedFortFloor() {
      return ((RedFortTemplate)((RedFort)this.getDungeon()).getTemplate()).getFloor(this.floor);
   }

   public boolean canChangeExteranl() {
      return false;
   }

   public void playerDie(Player player, Creature attacker) {
      ((RedFort)this.getDungeon()).playerDie(player.getID());
      int dieTimes = ((RedFort)this.getDungeon()).getDieTimes(player.getID());
      if (dieTimes >= 3) {
         ((RedFort)this.getDungeon()).removeSurvivePlayer(player.getID());
         RedFortKillRecord record = ((RedFort)this.getDungeon()).getRecord(player.getID());
         record.setTop(((RedFort)this.getDungeon()).getSurviveSize() + 1);
         ((RedFort)this.getDungeon()).pushResult(player);
      } else {
         super.playerDie(player, attacker);
      }

   }

   private void toNextFloor() {
      this.isToNext = true;
      RedFortFloor next = ((RedFortTemplate)((RedFort)this.getDungeon()).getTemplate()).getFloor(this.floor + 1);
      if (next == null) {
         ((RedFort)this.getDungeon()).doEnd();
      } else {
         RedFortMap nextMap = ((RedFort)this.getDungeon()).getRedfFortMap(this.floor + 1);
         Iterator it = this.getPlayerMap().values().iterator();

         while(it.hasNext()) {
            Player player = (Player)it.next();
            if (((RedFort)this.getDungeon()).getDieTimes(player.getID()) < 3) {
               this.switchMap(player, nextMap, nextMap.getBornPoint());
            }
         }

         nextMap.start();
         this.stopCheck();
         ((RedFort)this.getDungeon()).setCurrentFloor(nextMap.getFloor());
      }

   }

   public void stopCheck() {
      if (this.checkTimeFuture != null && !this.checkTimeFuture.isCancelled()) {
         this.checkTimeFuture.cancel(true);
         this.checkTimeFuture = null;
      }

   }

   public void writeRoleDetail(Player player, WriteOnlyPacket packet, Player viewer) throws Exception {
      packet.writeDouble((double)player.getID());
      packet.writeUTF("");
      packet.writeByte(player.getEvilEnumBeingSaw().getFont());
      packet.writeByte(-1);
      packet.writeUTF("");
      packet.writeShort(-1);
      packet.writeInt(player.getX());
      packet.writeInt(player.getY());
      packet.writeByte(player.getProfessionID());
      packet.writeBoolean(this.getUnitLandscape(player) == 2);
      packet.writeBoolean(player.isDie());
      List statList = StatList2Client.getAroundPlayerStats();
      packet.writeShort(statList.size());
      Iterator var6 = statList.iterator();

      while(var6.hasNext()) {
         StatEnum stat = (StatEnum)var6.next();
         int value = player.getStatValue(stat);
         PlayerAttributes.writeStat(stat, (long)value, packet);
      }

      ArrayList externalList = ((RedFortTemplate)((RedFort)this.getDungeon()).getTemplate()).getExternalEntryList(player.getProType());
      ExternalChange.writeRoleExternal(externalList, packet);
      packet.writeBoolean(player.isInRiding());
      AroundPlayer.writeBuffs(player, true, packet);
      packet.writeShort(-1);
      packet.writeShort(-1);
   }

   public void startCheck() {
      this.checkTimeFuture = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(new Runnable() {
         public void run() {
            RedFortMap.this.checkTime();
         }
      }, 0L, 1000L);
   }

   public void doEnterMapSpecil(Player player) {
      super.doEnterMapSpecil(player);
      player.getBuffManager().removeBuff(80003);
      if (this.getFloor() == 1 && !this.isStart) {
         int tmp = ((RedFort)this.getDungeon()).getStartTimeLeft();
         if (tmp > 0) {
            try {
               CenterMessage cm = new CenterMessage();
               cm.writeUTF(((RedFortTemplate)((RedFort)this.getDungeon()).getTemplate()).getPrepareStart().replace("%s%", String.valueOf(tmp)));
               player.writePacket(cm);
               cm.destroy();
               cm = null;
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }
      }

   }

   public void start() {
      this.setStart(true);
      this.startCheck();
      ((RedFort)this.getDungeon()).setCurFloorTime(((RedFortTemplate)((RedFort)this.getDungeon()).getTemplate()).getFloor(this.floor).getDuration());
   }

   public void setStart(boolean b) {
      this.isStart = b;
   }

   public void checkTime() {
      if (this.isStart) {
         int tmp = this.getRedFortFloor().getDuration() - this.timeCost;
         if (tmp > 0 && tmp <= 30 && tmp % 10 == 0 && this.getFloor() != ((RedFortTemplate)((RedFort)this.getDungeon()).getTemplate()).getMaxFloor()) {
            try {
               CenterMessage cm = new CenterMessage();
               cm.writeUTF(((RedFortTemplate)((RedFort)this.getDungeon()).getTemplate()).getPrepareToNext().replace("%s%", String.valueOf(tmp)));
               this.broadcastPacket(cm);
               cm.destroy();
               cm = null;
            } catch (Exception var3) {
               var3.printStackTrace();
            }
         }

         if (this.timeCost >= this.getRedFortFloor().getDuration() && !this.isToNext) {
            this.toNextFloor();
         }

         ++this.timeCost;
      }
   }

   public boolean petCanShow() {
      return false;
   }

   public int getFloor() {
      return this.floor;
   }

   public synchronized void destroy() {
      super.destroy();
      this.stopCheck();
   }

   public boolean canInTeam() {
      return false;
   }

   private void readyQuit(final Player player) {
      ((RedFort)this.getDungeon()).addRedayQuitFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
         public void run() {
            ((RedFort)RedFortMap.this.getDungeon()).exitForInitiative(player, true);
         }
      }, 2000L), player.getID());
   }

   public boolean canBeEnemy() {
      return false;
   }

   public int getRevivlContDown(Player player) {
      return ((RedFort)this.getDungeon()).getDieTimes(player.getID()) < 3 ? 3 : 60;
   }

   public boolean doReceive(Player player, int multiple) {
      RedFortKillRecord record = ((RedFort)this.getDungeon()).getRecord(player.getID());
      if (record.isHasReceived()) {
         return false;
      } else if (record.getTop() < 1) {
         if (((RedFort)this.getDungeon()).isComplete()) {
            this.readyQuit(player);
            return true;
         } else {
            return false;
         }
      } else {
         RedFortTopReward reward = ((RedFortTemplate)((RedFort)this.getDungeon()).getTemplate()).getTopReward(record.getTop());
         record.setHasReceived(true);
         if (reward != null && reward.getItemList().size() > 0 && !player.getItemManager().addItem((List)reward.getDataList()).isOk()) {
            SendMailTask.sendMail(player, player.getID(), MessageText.getText(14033), MessageText.getText(14034).replace("%s%", ((RedFortTemplate)((RedFort)this.getDungeon()).getTemplate()).getName()), reward.getItemList());
         }

         this.readyQuit(player);
         return true;
      }
   }
}
