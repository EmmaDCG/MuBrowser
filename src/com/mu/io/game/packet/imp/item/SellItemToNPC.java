package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.RepurchaseManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class SellItemToNPC extends ReadAndWritePacket {
   public SellItemToNPC(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemId = (long)this.readDouble();
      int count = this.readInt();
      int money = 0;
      boolean isBind = false;
      Storage backpack = player.getBackpack();
      Item item = backpack.getItemByID(itemId);
      int result = canSell(player, item, count);
      if (result == 1) {
         Item cloneItem = item.cloneItem(2);
         cloneItem.setCount(count);
         money = item.getMoney();
         money *= count;
         result = player.getItemManager().deleteItem(item, count, 18).getResult();
         if (result == 1) {
            RepurchaseManager.addSellItem(player, cloneItem);
         }
      }

      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

      if (isBind) {
         PlayerManager.addBindMoney(player, money);
      } else {
         PlayerManager.addMoney(player, money);
      }

   }

   public static int canSell(Player player, Item item, int count) {
      if (item != null && item.getCount() > 0) {
         if (!item.getModel().isCanSellToNpc()) {
            return 3014;
         } else {
            return count > 0 && item.getCount() >= count ? 1 : 3011;
         }
      } else {
         return 3002;
      }
   }
}
