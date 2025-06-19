package com.mu.game.model.hallow;

import com.mu.executor.Executor;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.hallow.model.HallowModel;
import com.mu.game.model.hallow.model.PartModel;
import com.mu.game.model.stats.StatChange;
import com.mu.game.model.stats.statId.StatIdCreator;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.dm.UpdateMenu;

public class HallowManager {
   private Player player;
   private int rank;
   private int level;

   public HallowManager(Player player) {
      this.player = player;
   }

   public void init(int rank, int level) {
      this.rank = Math.max(1, Math.min(rank, HallowModel.MaxRank));
      HallowModel model = HallowModel.getModel(this.rank);
      this.level = Math.max(0, Math.min(level, model.getMaxLevel()));
      this.addStats();
   }

   private void addStats() {
      if (this.isFunctionOpen()) {
         StatChange.addStat(this.getPlayer(), StatIdCreator.createHallowID(), PartModel.getPartModel(this.rank, this.level).getModifies(), true);
      }
   }

   public boolean isFunctionOpen() {
      return FunctionOpenManager.isOpen(this.getPlayer(), 24);
   }

   public void openFunction() {
      this.addStats();
      UpdateMenu.update(this.player, 33);
   }

   public int upLevel() {
      int result = this.canLevelUp(true);
      if (result != 1) {
         return result;
      } else {
         PartModel nextModel = this.getNextLevelPartModel();
         result = this.player.getItemManager().deleteItemByModel(nextModel.getItemModelID(), nextModel.getCount(), 42).getResult();
         if (result != 1) {
            return result;
         } else {
            ++this.level;
            this.init(this.rank, this.level);
            this.saveData();
            return 1;
         }
      }
   }

   public int upRank() {
      int result = this.canRankUp();
      if (result != 1) {
         return result;
      } else {
         ++this.rank;
         this.level = 0;
         this.init(this.rank, this.level);
         this.saveData();
         return 1;
      }
   }

   public void saveData() {
      WriteOnlyPacket packet = Executor.SaveHallow.toPacket(this.getPlayer());
      this.getPlayer().writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public int canRankUp() {
      if (!this.isFunctionOpen()) {
         return 23405;
      } else if (this.rank >= HallowModel.MaxRank) {
         return 23401;
      } else if (this.getModel().getMaxLevel() > this.getLevel()) {
         return 23402;
      } else {
         return this.player.getTanXianManager().getLevel() < this.getModel().getTreasureLevel() ? 23403 : 1;
      }
   }

   public int canLevelUp(boolean show) {
      if (!this.isFunctionOpen()) {
         return 23405;
      } else if (this.getLevel() >= this.getModel().getMaxLevel()) {
         return 23404;
      } else {
         if (!show) {
            PartModel nextModel = this.getNextLevelPartModel();
            if (!this.player.getBackpack().hasEnoughItem(nextModel.getItemModelID(), nextModel.getCount())) {
               return 3001;
            }
         }

         return 1;
      }
   }

   public int refine() {
      int type = 1;
      if (this.getLevel() >= this.getModel().getMaxLevel()) {
         type = 2;
      }

      int result;
      if (type == 1) {
         result = this.upLevel();
      } else {
         result = this.upRank();
      }

      return result;
   }

   public HallowModel getModel() {
      return HallowModel.getModel(this.rank);
   }

   public PartModel getCurPartModel() {
      return PartModel.getPartModel(this.rank, this.level);
   }

   public PartModel getNextLevelPartModel() {
      return PartModel.getPartModel(this.rank, this.level + 1);
   }

   public Player getPlayer() {
      return this.player;
   }

   public void setPlayer(Player player) {
      this.player = player;
   }

   public int getRank() {
      return this.rank;
   }

   public void setRank(int rank) {
      this.rank = rank;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }
}
