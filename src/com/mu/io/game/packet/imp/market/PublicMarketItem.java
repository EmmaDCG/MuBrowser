package com.mu.io.game.packet.imp.market;

import com.mu.config.Global;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.market.MarketItem;
import com.mu.game.model.market.MarketManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class PublicMarketItem extends ReadAndWritePacket {
   private static long interval = 10000L;

   public PublicMarketItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long now = System.currentTimeMillis();
      if (now - player.getLastMarketCallTime() < interval) {
         SystemMessage.writeMessage(player, 16620);
      } else {
         player.setLastMarketCallTime(now);
         if (Global.isInterServiceServer()) {
            SystemMessage.writeMessage(player, 16601);
         } else {
            long itemID = (long)this.readDouble();
            MarketItem mItem = MarketManager.getItem(itemID);
            if (mItem != null) {
               ChatProcess.exchangeCall(player, mItem);
            }
         }
      }
   }
}
