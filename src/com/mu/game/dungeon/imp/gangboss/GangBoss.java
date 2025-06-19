package com.mu.game.dungeon.imp.gangboss;

import com.mu.game.dungeon.Dungeon;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.schedule.SaveGangContributionTask;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;

public class GangBoss extends Dungeon {
   private GangBossGroup gb = null;
   private long gangId;

   public GangBoss(int id, GangBossTemplate t, GangBossGroup gb, long gangId) {
      super(id, t);
      this.gb = gb;
      this.gangId = gangId;
      this.keepNoCarePlayer = true;
      this.saveWhenInterrupt = false;
   }

   public void initMap() {
      GangBossMap map = new GangBossMap(this.gb.getMapId(), this);
      this.addMap(map);
      map.init();
   }

   public long getGangId() {
      return this.gangId;
   }

   public int getMoneyInspireFullMsg() {
      return 14053;
   }

   private GangBossMap getGangBossMap() {
      Map map = this.getFirstMap();
      return map == null ? null : (GangBossMap)this.getFirstMap();
   }

   public void checkTime() {
      super.checkTime();
      if (this.activeTime <= 0 && !this.isComplete()) {
         GangBossMap map = this.getGangBossMap();
         if (map != null) {
            map.setTimeIsOver(true);
         }
      }

   }

   public int checkMoneyInspire(Player player) {
      GangMember member = GangManager.getMember(player.getID());
      if (member != null && member.getCurContribution() >= ((GangBossTemplate)this.getTemplate()).getInspireMoney()) {
         member.reduceContribution(((GangBossTemplate)this.getTemplate()).getInspireMoney());
         SaveGangContributionTask.addMember(member.getId(), member.getCurContribution(), member.getHisContribution());
         return 1;
      } else {
         return 14052;
      }
   }

   public GangBossGroup getBossGroup() {
      return this.gb;
   }

   public DungeonResult createSuccessPacket() {
      return null;
   }

   public synchronized void destroy() {
      super.destroy();
   }
}
