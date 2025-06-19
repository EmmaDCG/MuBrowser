package com.mu.io.game.packet.imp.magicItem;

import com.mu.game.model.item.box.magic.market.MagicMarketGoods;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.Iterator;
import java.util.List;

public class ShowMagicMarketItems extends ReadAndWritePacket {
   public ShowMagicMarketItems(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      List goodsList = MagicMarketGoods.getGoodsList();
      this.writeShort(goodsList.size());
      Iterator var4 = goodsList.iterator();

      while(var4.hasNext()) {
         MagicMarketGoods goods = (MagicMarketGoods)var4.next();
         GetItemStats.writeItem(goods.getGoodItem(), this);
      }

      player.writePacket(this);
   }
}
