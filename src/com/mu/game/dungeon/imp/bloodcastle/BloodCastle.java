package com.mu.game.dungeon.imp.bloodcastle;

import com.mu.db.log.IngotChangeType;
import com.mu.game.dungeon.Dungeon;
import com.mu.game.dungeon.DungeonPlayerInfo;
import com.mu.game.dungeon.DungeonReward;
import com.mu.game.model.item.Item;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.dun.DunLogManager;
import com.mu.game.model.unit.player.popup.imp.DungeonQuitPopup;
import com.mu.io.game.packet.imp.dm.PointMenu;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.OpenPanel;
import java.util.ArrayList;
import java.util.Iterator;

public class BloodCastle extends Dungeon {
   private BloodCastleLevel level;
   private boolean isFaild = false;
   private boolean isReceived = false;

   public BloodCastle(int id, BloodCastleTemplate t, BloodCastleLevel level) {
      super(id, t);
      this.level = level;
      this.saveWhenInterrupt = true;
      this.timeLeft += 300000;
   }

   public BloodCastleMap getBloodCastleMap() {
      Map map = this.getFirstMap();
      return map != null ? (BloodCastleMap)map : null;
   }

   public void start() {
      BloodCastleMap map = this.getBloodCastleMap();
      if (map != null) {
         map.start();
      }

   }

   public boolean isFaild() {
      return this.isFaild;
   }

   public void setFaild(boolean isFaild) {
      this.isFaild = isFaild;
   }

   public void initMap() {
      BloodCastleMap map = new BloodCastleMap(this);
      this.addMap(map);
   }

   public BloodCastleLevel getBloodCastleLevel() {
      return this.level;
   }

   public IngotChangeType getInspireIngotReduceType() {
      return IngotChangeType.BloodInspire;
   }

   public void checkTime() {
      super.checkTime();
      BloodCastleMap bcm = this.getBloodCastleMap();
      if (bcm != null && !this.isSuccess()) {
         bcm.pushSchedule();
      }

   }

   public void doReceive(Player player) {
      if (!this.isReceived) {
         DungeonReward reward = this.getBloodCastleLevel().getReward(((BloodCastleTemplate)this.getTemplate()).getRankByLeftTime(this.getActiveTime()).getRank());
         if (reward != null) {
            DungeonPlayerInfo info = this.getDungeonPlayerInfo(player.getID());
            if (info != null && !info.hasRewad()) {
               long exp = reward.getExp();
               int money = reward.getMoney();
               PlayerManager.addExp(player, exp, -1L);
               if (money > 0) {
                  PlayerManager.addMoney(player, money);
               }

               this.isReceived = true;
            }
         }
      }
   }

   public Player removePlayer(Player player) {
      if (player == null) {
         return player;
      } else {
         try {
            if (this.isSuccess()) {
               OpenPanel.open(player, 69, 0);
            }

            DunLogManager manager = player.getDunLogsManager();
            if (manager != null && manager.getTotalNumber(1) == 1) {
               PointMenu.pointMenu(player, ((BloodCastleTemplate)this.getTemplate()).getPointMenu(), ((BloodCastleTemplate)this.getTemplate()).getPointMsg());
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         return super.removePlayer(player);
      }
   }

   public DungeonResult createSuccessPacket() {
      try {
         DungeonResult ds = new DungeonResult();
         ds.writeByte(2);
         ds.writeBoolean(true);
         ds.writeInt(this.getTimeCost());
         int rank = ((BloodCastleTemplate)this.getTemplate()).getRankByLeftTime(this.getActiveTime()).getRank();
         ds.writeByte(rank);
         BloodCastleLevel level = this.getBloodCastleLevel();
         DungeonReward reward = level.getReward(rank);
         ds.writeInt(reward.getMoney());
         ds.writeDouble((double)reward.getExp());
         ds.writeUTF("");
         ArrayList iList = reward.getItemList();
         ds.writeByte(iList.size());
         Iterator var7 = iList.iterator();

         while(var7.hasNext()) {
            Item item = (Item)var7.next();
            GetItemStats.writeItem(item, ds);
         }

         return ds;
      } catch (Exception var8) {
         var8.printStackTrace();
         return null;
      }
   }

   public void exitForInitiative(Player player, boolean force) {
      if (!force && !this.isComplete() && !this.isFaild) {
         DungeonQuitPopup pop = new DungeonQuitPopup(player.createPopupID());
         ShowPopup.open(player, pop);
      } else {
         super.exitForInitiative(player, force);
      }
   }

   public void exitForIntterupt(Player player) {
      super.exitForIntterupt(player);
   }
}
