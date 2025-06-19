package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.RepurchaseManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class RepurchaseItemFromNpc extends ReadAndWritePacket {
   public RepurchaseItemFromNpc(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      Item item = RepurchaseManager.getItem(player.getID(), itemID);
      if (item == null) {
         SystemMessage.writeMessage(player, 3015);
      } else {
         int money = item.getMoney() * item.getCount();
         int result = PlayerManager.hasEnoughMoney(player, money) ? 1 : 1011;
         if (result != 1) {
            SystemMessage.writeMessage(player, result);
         } else {
            result = player.getItemManager().addItem(item, 19).getResult();
            if (result == 1) {
               PlayerManager.reduceMoney(player, money);
            }

            if (result != 1) {
               SystemMessage.writeMessage(player, result);
            }

         }
      }
   }
}
