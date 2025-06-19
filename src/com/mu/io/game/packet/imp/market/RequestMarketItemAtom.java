package com.mu.io.game.packet.imp.market;

import com.mu.game.model.market.MarketItem;
import com.mu.game.model.market.MarketManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class RequestMarketItemAtom extends ReadAndWritePacket {
   public RequestMarketItemAtom(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      MarketItem mItem = MarketManager.getItem(itemID);
      this.writeBoolean(mItem != null);
      if (mItem != null) {
         RequestMarketItem.writeItemDetail(mItem, this);
      }

      player.writePacket(this);
   }
}
