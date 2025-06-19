package com.mu.io.game.packet.imp.market;

import com.mu.config.Constant;
import com.mu.config.Global;
import com.mu.db.log.IngotChangeType;
import com.mu.db.manager.PlayerDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.market.MarketItem;
import com.mu.game.model.market.MarketManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.DeleteItem;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class BuyMarketItem extends ReadAndWritePacket {
   public BuyMarketItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      MarketItem item = MarketManager.getItem(itemID);
      int result = this.canBuy(player, item);
      long newItemID = 0L;
      if (result == 1) {
         Item tmpItem = item.getItem().cloneItem(2);
         OperationResult or = player.getItemManager().addItem(tmpItem, 30);
         result = or.getResult();
         newItemID = or.getItemID();
         if (result == 1) {
            PlayerManager.reduceIngot(player, item.getPrice(), IngotChangeType.Market, IngotChangeType.getItemLogDetail(tmpItem.getModelID()));
            MarketManager.removeMarketItem(itemID);
            int addIngot = item.getPrice();
            int tax = Constant.getPercentValue(addIngot, item.getTaxRate());
            tax = Math.max(1, tax);
            addIngot -= tax;
            addIngot = Math.max(1, addIngot);
            Player owner = CenterManager.getPlayerByRoleID(item.getRoleID());
            if (owner != null) {
               PlayerManager.addIngot(owner, addIngot, IngotChangeType.Market.getType());
               DeleteItem.sendToClient(owner, itemID, 11);
            } else {
               PlayerDBManager.saveOffLinePlayerIngot(item.getUserName(), item.getServerID(), addIngot);
            }

            MarketManager.addRecord(item, player, tax);
         }
      }

      this.writeBoolean(result == 1);
      if (result == 1) {
         this.writeDouble((double)newItemID);
      } else {
         SystemMessage.writeMessage(this.getPlayer(), result);
      }

      player.writePacket(this);
   }

   private int canBuy(Player player, MarketItem item) {
      if (Global.isInterServiceServer()) {
         return 16601;
      } else if (item == null) {
         return 16609;
      } else if (player.getIngot() < item.getPrice()) {
         return 1015;
      } else if (item.getRoleID() == player.getID()) {
         return 16610;
      } else if (item.getUserName().equals(player.getUserName())) {
         return 16621;
      } else if (player.getBackpack().isFull()) {
         return 2004;
      } else {
         return item.getItem().getModel().isMoney() ? 16606 : 1;
      }
   }
}
