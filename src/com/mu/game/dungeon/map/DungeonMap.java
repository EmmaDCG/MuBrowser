package com.mu.game.dungeon.map;

import com.mu.executor.imp.buff.SaveBuffWhenOffLineExecutor;
import com.mu.game.dungeon.Dungeon;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.buff.Buff;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.offline.OfflineBuffInfo;
import com.mu.game.model.unit.player.offline.OfflineManager;
import com.mu.io.game.packet.imp.dungeon.DungeonInspire;
import java.awt.Point;

public abstract class DungeonMap extends SubMap {
   private Dungeon dungeon;
   protected int mapIndex;

   public DungeonMap(int referMapID, Dungeon d) {
      super(referMapID);
      this.dungeon = d;
      this.setCanPk(false);
   }

   public void setMapIndex(int mapIndex) {
      this.mapIndex = mapIndex;
   }

   public synchronized void destroy() {
      if (!this.isDestroy) {
         super.destroy();
         this.dungeon = null;
      }
   }

   public Dungeon getDungeon() {
      return this.dungeon;
   }

   public int getMapIndex() {
      return this.mapIndex;
   }

   public void doEnterMapSpecil(Player player) {
      super.doEnterMapSpecil(player);
      OfflineBuffInfo info = OfflineManager.getBuffInfo(player.getVipShowLevel());
      Buff buff = player.getBuffManager().getBuff(info.getBuffId());
      if (buff != null) {
         this.getDungeon().addOfflineBuff(player.getID(), info.getBuffId(), buff.getLevel(), SaveBuffWhenOffLineExecutor.getRemainTime(buff, System.currentTimeMillis()));
         player.getBuffManager().endBuff(info.getBuffId(), true);
      }

      if (this.getDungeon().getTemplate().isCanInspire()) {
         DungeonInspire.pushInspireInfo(this.getDungeon().getTemplate(), this.getDungeon().getDungeonPlayerInfo(player.getID()), player);
      }

   }

   public final int getMapType() {
      return 2;
   }

   public boolean switchMap(Player player, Map targetMap, Point position) {
      if (!this.getDungeon().mapInSelfDunegon(targetMap)) {
         this.getDungeon().exitForSwitchMap(player);
      }

      return super.switchMap(player, targetMap, position);
   }

   public void addPlayer(Player player, Point p) {
      player.setMap(this);
      player.setPosition(p.x, p.y);
      this.playerMap.put(player.getID(), player);
      if (!this.getDungeon().hasPlayer(player.getID())) {
         this.getDungeon().addPlayer(player);
      }

   }
}
