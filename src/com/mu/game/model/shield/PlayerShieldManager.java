package com.mu.game.model.shield;

import com.mu.executor.Executor;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.mall.MallConfigManager;
import com.mu.game.model.mall.MallItemData;
import com.mu.game.model.stats.StatChange;
import com.mu.game.model.stats.statId.StatIdCreator;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.popup.imp.MallBuyPopup;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.shield.InitShield;
import com.mu.io.game.packet.imp.shield.ShieldInform;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerShieldManager {
   private static Logger logger = LoggerFactory.getLogger(PlayerShieldManager.class);
   private Player owner;
   private ShieldLevel level;
   private ShieldRank rank;
   private int rankExp;

   public PlayerShieldManager(Player owner) {
      this.owner = owner;
   }

   public void onLoginInit(InitShield packet) {
      try {
         boolean has = packet.readBoolean();
         if (has) {
            int level = packet.readInt();
            int rank = packet.readInt();
            int star = packet.readInt();
            int rankExp = packet.readInt();
            this.level = ShieldConfigManager.getLevel(level);
            this.rank = ShieldConfigManager.getRank(rank, star);
            this.rankExp = rankExp;
         } else {
            this.level = ShieldConfigManager.getLevel(1);
            this.rank = ShieldConfigManager.getRank(1, 0);
            this.rankExp = 0;
         }

         if (FunctionOpenManager.isOpen(this.owner, 19)) {
            this.addStat();
         }
      } catch (Exception var7) {
         logger.error("role init pet data error!");
         var7.printStackTrace();
      }

   }

   public void addStat() {
      StatChange.addStat(this.owner, StatIdCreator.createShieldId(0), this.level.getPropertyList(), StatChange.isSendStat(this.owner));
      StatChange.addStat(this.owner, StatIdCreator.createShieldId(1), this.rank.getPropertyList2(), StatChange.isSendStat(this.owner));
   }

   public boolean riseLevel() {
      if (this.getOwner().getMoney() < this.level.getExpend()) {
         SystemMessage.writeMessage(this.owner, 6101);
         return false;
      } else {
         ShieldLevel next = ShieldConfigManager.getLevel(this.level.getLevel() + 1);
         if (next != null && next == this.level) {
            SystemMessage.writeMessage(this.owner, 6102);
            return false;
         } else if (1 != PlayerManager.reduceMoney(this.getOwner(), this.level.getExpend())) {
            SystemMessage.writeMessage(this.owner, 6101);
            return false;
         } else {
            this.level = next;
            StatChange.addStat(this.owner, StatIdCreator.createShieldId(0), this.level.getPropertyList(), StatChange.isSendStat(this.owner));
            this.dbReplaceShield();
            return true;
         }
      }
   }

   public void riseRank() {
      if (this.rank.getNext() == null) {
         SystemMessage.writeMessage(this.owner, 6103);
      } else if (!this.owner.getItemManager().deleteItemByModel(this.rank.getExpendItemId(), this.rank.getExpendItemCount(), 19).isOk()) {
         MallItemData data = MallConfigManager.getData(this.rank.getExpendItemId());
         if (data != null) {
            int tmpCount = this.owner.getBackpack().getItemCount(this.rank.getExpendItemId());
            int count = this.rank.getExpendItemCount() - tmpCount;
            if (count > 0) {
               ShowPopup.open(this.owner, new MallBuyPopup(this.owner.createPopupID(), data, count));
            }
         }

      } else {
         int exp = this.rankExp + this.rank.getAddValue();
         if (exp >= this.rank.getLimit()) {
            this.rankExp = exp - this.rank.getLimit();
            this.rank = this.rank.getNext();
            StatChange.addStat(this.owner, StatIdCreator.createShieldId(1), this.rank.getPropertyList2(), StatChange.isSendStat(this.owner));
            ShieldInform.sendMsgShieldRankEXP(this.owner, true, this.rank.getLimit(), this.rankExp);
            ShieldInform.sendMsgShieldInform(this);
         } else {
            this.rankExp = exp;
            ShieldInform.sendMsgShieldRankEXP(this.owner, false, this.rank.getLimit(), this.rankExp);
         }

         this.dbReplaceShield();
      }
   }

   public Player getOwner() {
      return this.owner;
   }

   public ShieldLevel getLevel() {
      return this.level;
   }

   public ShieldRank getRank() {
      return this.rank;
   }

   public int getRankExp() {
      return this.rankExp;
   }

   public void dbReplaceShield() {
      try {
         WriteOnlyPacket packet = Executor.ReplaceShield.toPacket(this.getOwner().getID(), this.level.getLevel(), this.rank.getRank(), this.rank.getStar(), this.rankExp);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
