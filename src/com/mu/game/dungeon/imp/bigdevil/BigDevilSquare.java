package com.mu.game.dungeon.imp.bigdevil;

import com.mu.db.log.IngotChangeType;
import com.mu.game.dungeon.Dungeon;
import com.mu.game.dungeon.DungeonPlayerInfo;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.DungeonQuitPopup;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.CenterMessage;

public class BigDevilSquare extends Dungeon {
   private BigDevilSquareLevel dl = null;
   private int prepareTime = 10;
   private boolean isBegin = false;
   private int totalActiveTime = 0;

   public BigDevilSquare(int id, BigDevilSquareTemplate t, BigDevilSquareLevel dl) {
      super(id, t);
      this.dl = dl;
      this.saveWhenInterrupt = true;
      this.activeTime += this.prepareTime;
   }

   public BigDevilSquareLevel getSquareLevel() {
      return this.dl;
   }

   public void setActiveTime(int time) {
      super.setActiveTime(time);
      this.totalActiveTime = time;
   }

   public int getStartTimeLeft() {
      return this.totalActiveTime - this.timeLimit - this.timeCost;
   }

   public void initMap() {
      BigDevilSquareMap map = new BigDevilSquareMap(this);
      this.addMap(map);
   }

   public BigDevilSquareMap getDevilSquareMap() {
      Map map = this.getFirstMap();
      return map == null ? null : (BigDevilSquareMap)map;
   }

   public void addTotalExp(Player player, long exp) {
      DungeonPlayerInfo info = this.getDungeonPlayerInfo(player.getID());
      info.addExp(exp);
   }

   public IngotChangeType getInspireIngotReduceType() {
      return IngotChangeType.DevilInspire;
   }

   public void checkTime() {
      BigDevilSquareMap dsm = this.getDevilSquareMap();
      int startTimeLeft = this.getStartTimeLeft();
      if (startTimeLeft <= 0 && !this.isBegin) {
         this.isBegin = true;
         dsm.start();
      }

      if (startTimeLeft > 0 && startTimeLeft % 60 == 0) {
         try {
            CenterMessage cm = new CenterMessage();
            cm.writeUTF(((BigDevilSquareTemplate)this.getTemplate()).getEnterMsg().replace("%s%", String.valueOf(startTimeLeft / 60)));
            this.broadcastPacket(cm);
            cm.destroy();
            cm = null;
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      if (this.getActiveTime() <= 0 && !this.isSuccess()) {
         dsm.doSuccess();
         ((BigDevilSquareTemplate)this.getTemplate()).getBigDevilManager().reduceSquareNumber();
      }

      if (this.getActiveTime() >= 0) {
         dsm.pushSchedule();
      }

      super.checkTime();
   }

   public DungeonResult createSuccessPacket() {
      return null;
   }

   public synchronized void destroy() {
      super.destroy();
      ((BigDevilSquareTemplate)this.getTemplate()).getBigDevilManager().removeSquare(this.getID());
      this.dl = null;
   }

   public void exitForInitiative(Player player, boolean force) {
      if (!force && !this.isComplete()) {
         DungeonQuitPopup pop = new DungeonQuitPopup(player.createPopupID());
         ShowPopup.open(player, pop);
      } else {
         if (!this.isSuccess()) {
            ((BigDevilSquareTemplate)this.getTemplate()).getBigDevilManager().changeHistoryInfo();
         }

         super.exitForInitiative(player, force);
      }
   }

   public void exitForIntterupt(Player player) {
      super.exitForIntterupt(player);
   }
}
