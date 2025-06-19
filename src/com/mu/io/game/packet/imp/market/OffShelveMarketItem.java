package com.mu.io.game.packet.imp.market;

import com.mu.config.Global;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.market.MarketItem;
import com.mu.game.model.market.MarketManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.DeleteItem;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class OffShelveMarketItem extends ReadAndWritePacket {
   public OffShelveMarketItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      MarketItem mItem = MarketManager.getItem(itemID);
      int result = this.canOff(player, mItem);
      long newItemID = 0L;
      if (result == 1) {
         Item tmpItem = mItem.getItem().cloneItem(2);
         tmpItem.setMoneyType(1);
         OperationResult or = player.getItemManager().addItem(tmpItem, 30);
         result = or.getResult();
         newItemID = or.getItemID();
         if (result == 1) {
            DeleteItem.sendToClient(player, itemID, 11);
            MarketManager.removeMarketItem(itemID);
         }
      }

      this.writeBoolean(result == 1);
      if (result == 1) {
         this.writeDouble((double)newItemID);
      } else {
         SystemMessage.writeMessage(player, result);
      }

      player.writePacket(this);
   }

   private int canOff(Player player, MarketItem mItem) {
      if (Global.isInterServiceServer()) {
         return 16601;
      } else if (mItem == null) {
         return 16609;
      } else if (mItem.getRoleID() != player.getID()) {
         return 16611;
      } else {
         return player.getBackpack().isFull() ? 2004 : 1;
      }
   }
}
