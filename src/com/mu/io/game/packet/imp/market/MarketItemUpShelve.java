package com.mu.io.game.packet.imp.market;

import com.mu.config.Global;
import com.mu.db.manager.MarketDBManager;
import com.mu.game.IDFactory;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.market.MarketConstant;
import com.mu.game.model.market.MarketItem;
import com.mu.game.model.market.MarketManager;
import com.mu.game.model.market.MarketSort;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.AddItem;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.concurrent.ThreadCachedPoolManager;

public class MarketItemUpShelve extends ReadAndWritePacket {
   private int slot = -1;

   public MarketItemUpShelve(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      boolean isMoney = this.readBoolean();
      long itemID = 0L;
      if (!isMoney) {
         itemID = (long)this.readDouble();
      }

      int count = this.readInt();
      int price = this.readInt();
      int result = this.canUp(player, isMoney, itemID, count, price);
      Item item = null;
      if (result == 1) {
         if (isMoney) {
            item = ItemTools.createItem(2015, count, 2);
         } else {
            item = player.getBackpack().getItemByID(itemID);
         }

         result = this.canUp2(player, item, count);
      }

      if (result == 1) {
         Item tmpItem = null;
         if (isMoney) {
            tmpItem = item;
            item.setId(IDFactory.getItemID());
         } else {
            tmpItem = item.cloneItem(2);
            if (count < item.getCount()) {
               tmpItem.setId(IDFactory.getItemID());
            } else {
               tmpItem.setId(item.getID());
            }
         }

         tmpItem.setContainerType(11);
         tmpItem.setCount(count);
         tmpItem.setSlot(this.slot);
         tmpItem.setMoneyType(2);
         tmpItem.setShopMoney(price);
         if (isMoney) {
            result = PlayerManager.reduceMoney(player, count);
         } else {
            result = player.getItemManager().deleteItem(item, count, 30).getResult();
         }

         if (result == 1) {
            AddItem.sendToClient(player, tmpItem);
            final MarketItem marketItem = new MarketItem(tmpItem);
            marketItem.setShelveTime(System.currentTimeMillis());
            marketItem.setRoleID(player.getID());
            marketItem.setOwnerName(player.getName());
            marketItem.setUserName(player.getUserName());
            marketItem.setServerID(player.getUser().getServerID());
            marketItem.setTaxRate(MarketConstant.getPersonTaxRate(player));
            MarketManager.addMarketItem(marketItem);
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  MarketDBManager.insertMarketItem(marketItem);
               }
            });
         }
      }

      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

      this.writeBoolean(result == 1);
      player.writePacket(this);
   }

   private int canUp(Player player, boolean isMoney, long itemID, int count, int price) {
      if (Global.isInterServiceServer()) {
         return 16601;
      } else if (count < 1) {
         return 16604;
      } else if (isMoney) {
         return 16606;
      } else if (price < 2) {
         return 16608;
      } else {
         this.slot = MarketManager.getNextSlot(player.getID());
         return this.slot == -1 ? 16607 : 1;
      }
   }

   private int canUp2(Player player, Item item, int count) {
      if (item == null) {
         return 3002;
      } else if (item.getCount() < count) {
         return 16604;
      } else if (item.isTimeExpired(System.currentTimeMillis())) {
         return 3044;
      } else if (item.isBind()) {
         return 16605;
      } else if (!MarketSort.getCanSellItemTypes().contains(item.getItemType())) {
         return 16606;
      } else if (!item.getModel().isCanExchange()) {
         return 16606;
      } else if (item.getExpireTime() != -1L) {
         return 16619;
      } else {
         this.slot = MarketManager.getNextSlot(player.getID());
         return this.slot == -1 ? 16607 : 1;
      }
   }
}
