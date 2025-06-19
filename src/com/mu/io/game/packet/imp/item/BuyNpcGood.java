package com.mu.io.game.packet.imp.item;

import com.mu.db.log.IngotChangeType;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.shop.Goods;
import com.mu.game.model.shop.ShopConfigure;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.pkMode.EvilEnum;
import com.mu.game.model.unit.player.popup.imp.RedNamePopup;
import com.mu.game.model.unit.service.EvilManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class BuyNpcGood extends ReadAndWritePacket {
   public BuyNpcGood(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int shopID = this.readByte();
      long itemId = (long)this.readDouble();
      int count = this.readInt();
      Goods goods = ShopConfigure.getGoods(itemId);
      int result = buyGoods(player, goods, shopID, count, false);
      if (result != 1 && result != 3022) {
         SystemMessage.writeMessage(player, result);
      }

   }

   public static int buyGoods(Player player, Goods goods, int shopID, int count, boolean fromHang) {
      int result = canBuy(player, shopID, goods, count, fromHang);
      if (result == 1) {
         ItemDataUnit unit = goods.getUnit();
         unit.setCount(count);
         result = player.getItemManager().addItem(unit).getResult();
         if (result == 1) {
            int money = goods.getPrice() * count;
            switch(goods.getMoneyType()) {
            case 2:
               String reduceDetail = IngotChangeType.getItemLogDetail(unit.getModelID()) + "," + count;
               PlayerManager.reduceIngot(player, money, IngotChangeType.BuyItemFromNPC, reduceDetail);
               break;
            default:
               PlayerManager.reduceMoney(player, money);
            }
         }
      }

      return result;
   }

   private static int canBuy(Player player, int shopID, Goods goods, int count, boolean fromHang) {
      if (goods == null) {
         return 3018;
      } else if (player.getSelfEvilEnum() != EvilEnum.Evil_Red) {
         int money = goods.getPrice() * count;
         switch(goods.getMoneyType()) {
         case 2:
            if (player.getIngot() < money) {
               return 1015;
            }
            break;
         default:
            if (!PlayerManager.hasEnoughMoney(player, money)) {
               return 1011;
            }
         }

         return 1;
      } else {
         if (!fromHang || player.isNeedtoPopPKEvil()) {
            RedNamePopup pop = new RedNamePopup(player.createPopupID(), EvilManager.getShowEvil(player.getEvil()));
            ShowPopup.open(player, pop);
            player.setNeedtoPopPKEvil(false);
         }

         return 3022;
      }
   }
}
