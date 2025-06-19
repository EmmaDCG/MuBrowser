package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class QuickSaleCalMoney extends ReadAndWritePacket {
   public QuickSaleCalMoney(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Storage backpack = player.getBackpack();
      int result = 1;
      int money = 0;
      int size = this.readShort();

      for(int i = 0; i < size; ++i) {
         long itemID = (long)this.readDouble();
         Item item = backpack.getItemByID(itemID);
         if (item == null) {
            result = 3002;
            break;
         }

         money += item.getMoney();
      }

      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

      this.writeInt(money);
      player.writePacket(this);
   }
}
