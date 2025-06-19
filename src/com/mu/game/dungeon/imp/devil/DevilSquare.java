package com.mu.game.dungeon.imp.devil;

import com.mu.db.log.IngotChangeType;
import com.mu.game.dungeon.Dungeon;
import com.mu.game.dungeon.DungeonPlayerInfo;
import com.mu.game.dungeon.DungeonReward;
import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.guide.arrow.ArrowInfo;
import com.mu.game.model.item.Item;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.popup.imp.DungeonQuitPopup;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.OpenPanel;
import java.util.ArrayList;
import java.util.Iterator;

public class DevilSquare extends Dungeon {
   private DevilSquareLevel dl = null;
   private int prepareTime = 10;
   private boolean isBegin = false;
   private int killNum = 0;
   private long totalExp = 0L;
   private boolean isReceived = false;

   public DevilSquare(int id, DevilSquareTemplate t, DevilSquareLevel dl) {
      super(id, t);
      this.dl = dl;
      this.saveWhenInterrupt = true;
      this.activeTime += this.prepareTime;
   }

   public DevilSquareLevel getSquareLevel() {
      return this.dl;
   }

   public boolean ifMoneyInspireSuccess(Player player) {
      ArrowGuideManager manager = player.getArrowGuideManager();
      ArrowInfo info = ArrowGuideManager.getArrowInfo(23);
      return info != null && manager.getGuideTimes(info.getId()) <= info.getTimes() ? true : super.ifMoneyInspireSuccess(player);
   }

   public int inspire(Player player, int type) {
      int result = super.inspire(player, type);
      ArrowGuideManager manager = player.getArrowGuideManager();
      ArrowInfo info = ArrowGuideManager.getArrowInfo(23);
      if (info != null && manager.getGuideTimes(info.getId()) <= info.getTimes()) {
         ArrowGuideManager.pushArrow(player, info.getId(), (String)null);
      }

      return result;
   }

   public void initMap() {
      DevilSquareMap map = new DevilSquareMap(this);
      this.addMap(map);
   }

   public DevilSquareMap getDevilSquareMap() {
      Map map = this.getFirstMap();
      return map == null ? null : (DevilSquareMap)map;
   }

   public IngotChangeType getInspireIngotReduceType() {
      return IngotChangeType.DevilInspire;
   }

   public void checkTime() {
      super.checkTime();
      DevilSquareMap dsm = this.getDevilSquareMap();
      if (this.timeCost >= this.prepareTime && !this.isBegin) {
         this.isBegin = true;
         dsm.start();
      }

      if (this.getActiveTime() <= 0 && dsm != null) {
         if (this.isSuccess()) {
            return;
         }

         dsm.doSuccess();
      }

      if (dsm != null) {
         dsm.pushSchedule();
      }

   }

   public DungeonResult createSuccessPacket() {
      DevilSquareMap dsm = this.getDevilSquareMap();
      if (dsm == null) {
         return null;
      } else {
         DungeonResult ds = new DungeonResult();

         try {
            ds.writeByte(2);
            ds.writeBoolean(true);
            ds.writeInt(this.getTimeCost());
            int rank = ((DevilSquareTemplate)this.getTemplate()).getRank(this.getKillNum()).getRank();
            ds.writeByte(rank);
            DevilSquareLevel level = this.getSquareLevel();
            DungeonReward reward = level.getReward(rank);
            ds.writeInt(reward.getMoney());
            ds.writeDouble((double)reward.getExp());
            ds.writeUTF("");
            ArrayList iList = reward.getItemList();
            ds.writeByte(iList.size());
            Iterator var8 = iList.iterator();

            while(var8.hasNext()) {
               Item item = (Item)var8.next();
               GetItemStats.writeItem(item, ds);
            }

            return ds;
         } catch (Exception var9) {
            var9.printStackTrace();
            return null;
         }
      }
   }

   public Player removePlayer(Player player) {
      if (this.isSuccess()) {
         OpenPanel.open(player, 68, 0);
      }

      return super.removePlayer(player);
   }

   public void addTotalExp(long exp) {
      this.totalExp += exp;
   }

   public void doReceive(Player player) {
      if (!this.isReceived) {
         DungeonReward reward = this.getSquareLevel().getReward(((DevilSquareTemplate)this.getTemplate()).getRank(this.killNum).getRank());
         if (reward != null) {
            DungeonPlayerInfo info = this.getDungeonPlayerInfo(player.getID());
            if (info != null && !info.hasRewad()) {
               long exp = reward.getExp();
               int money = reward.getMoney();
               if (!this.isComplete()) {
                  money = 0;
               }

               if (exp > 0L) {
                  PlayerManager.addExp(player, exp, -1L);
               }

               if (money > 0) {
                  PlayerManager.addMoney(player, money);
               }

               this.isReceived = true;
            }
         }
      }
   }

   public void updateUnReceivedLogs(Player player) {
   }

   public int getKillNum() {
      return this.killNum;
   }

   public void addKillNum() {
      ++this.killNum;
   }

   public long getTotalExp() {
      return this.totalExp;
   }

   public void exitForInitiative(Player player, boolean force) {
      if (!force && !this.isComplete()) {
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
