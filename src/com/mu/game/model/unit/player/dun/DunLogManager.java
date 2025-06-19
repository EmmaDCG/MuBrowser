package com.mu.game.model.unit.player.dun;

import com.mu.executor.Executor;
import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.utils.Time;
import java.util.HashMap;

public class DunLogManager {
   private Player owner = null;
   private HashMap logMap = new HashMap();
   private HashMap totalLogMap = new HashMap();

   public DunLogManager(Player owner) {
      this.owner = owner;
   }

   public void destroy() {
      this.owner = null;
      if (this.logMap != null) {
         this.logMap.clear();
         this.logMap = null;
      }

      if (this.totalLogMap != null) {
         this.totalLogMap.clear();
         this.totalLogMap = null;
      }

   }

   public Player getOwner() {
      return this.owner;
   }

   public void addLog(DunLogs log) {
      this.logMap.put(log.getDunId() + "_" + log.getSmallId(), log);
   }

   public DunLogs getLog(int dunId) {
      return this.getLog(dunId, 0);
   }

   public DunLogs getLog(int dunId, int smallId) {
      return (DunLogs)this.logMap.get(dunId + "_" + smallId);
   }

   public int getTotalNumber(int dunId, int smallId) {
      Integer in = (Integer)this.totalLogMap.get(dunId + "_" + smallId);
      return in == null ? 0 : in.intValue();
   }

   public int getTotalNumber(int dunId) {
      return this.getTotalNumber(dunId, 0);
   }

   public void setTotalNumber(int dunId, int smallId, int num) {
      String tag = dunId + "_" + smallId;
      this.totalLogMap.put(tag, num);
   }

   public void addTotalNumber(int dunId, int smallId) {
      String tag = dunId + "_" + smallId;
      int value = 1;
      Integer in = (Integer)this.totalLogMap.get(dunId + "_" + smallId);
      if (in != null) {
         value = in.intValue() + 1;
      }

      this.totalLogMap.put(tag, value);
      WriteOnlyPacket packet = Executor.SaveDunTotalLogs.toPacket(this.owner.getID(), dunId, smallId, value);
      this.owner.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public void finishDungeon(int dunId, int smallId) {
      String tag = dunId + "_" + smallId;
      DunLogs logs = (DunLogs)this.logMap.get(tag);
      if (logs == null) {
         logs = new DunLogs(dunId);
         logs.setSmallId(smallId);
         this.logMap.put(tag, logs);
      }

      long now = System.currentTimeMillis();
      logs.setFinishTimes(logs.getFinishTimes() + 1);
      logs.setLastFinishTime(now);
      logs.setSaveDay(Time.getDayLong());
      logs.setVipLevel(this.getOwner().getVipShowLevel());
      WriteOnlyPacket packet = Executor.SaveDunLogs.toPacket(this.owner.getID(), logs);
      this.owner.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public int getMaxtimes(int dunTemplateId, int smallId) {
      DungeonTemplate template = DungeonTemplateFactory.getTemplate(dunTemplateId);
      return template == null ? 0 : template.getMaxTimes(this.owner, smallId);
   }

   public void clearLogs() {
      this.logMap.clear();
   }
}
