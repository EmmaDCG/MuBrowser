package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;
import java.util.List;

public class QuickSale extends ReadAndWritePacket {
   public QuickSale(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int size = this.readShort();
      List itemList = new ArrayList();
      Storage backpack = player.getBackpack();
      int result = 1;
      int money = 0;

      for(int i = 0; i < size; ++i) {
         long itemID = (long)this.readDouble();
         Item item = backpack.getItemByID(itemID);
         if (item == null) {
            result = 3002;
            break;
         }

         money += item.getMoney();
         itemList.add(item);
      }

      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      } else {
         result = player.getItemManager().deleteItemList(itemList, 18).getResult();
         if (result == 1) {
            PlayerManager.addMoney(player, money);
         }
      }

      itemList.clear();
      itemList = null;
   }
}
