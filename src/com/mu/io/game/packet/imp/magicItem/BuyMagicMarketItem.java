package com.mu.io.game.packet.imp.magicItem;

import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.box.magic.market.MagicMarketGoods;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;

public class BuyMagicMarketItem extends ReadAndWritePacket {
   int money = 0;

   public BuyMagicMarketItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      int count = 1;
      MagicMarketGoods goods = MagicMarketGoods.getGoods(itemID);
      int result = this.canBuy(player, goods, count);
      if (result == 1) {
         ItemDataUnit unit = goods.getUnit();
         unit.setCount(count);
         result = player.getItemManager().addItem(unit).getResult();
         if (result == 1) {
            PlayerManager.reduceRedeemPoints(player, this.money);
         }
      }

      if (result == 1) {
         this.writeDouble((double)itemID);
         player.writePacket(this);
      }

   }

   private int canBuy(Player player, MagicMarketGoods goods, int count) {
      if (goods == null) {
         return 3060;
      } else if (count < 1) {
         return 2013;
      } else {
         this.money = goods.getPrice() * count;
         return player.getRedeemPoints() < this.money ? 3061 : 1;
      }
   }
}
