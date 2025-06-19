package com.mu.io.game.packet.imp.mall;

import com.mu.db.log.IngotChangeType;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.mall.ShortcutBuyPanel;
import com.mu.game.model.shop.Goods;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class ShortcutBuyAndUse extends ReadAndWritePacket {
   public ShortcutBuyAndUse(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long id = (long)this.readDouble();
      int result = this.canBuy(player, id);
      if (result == 1) {
         Goods item = ShortcutBuyPanel.getSellItem(id);
         int source = 33;
         int money = item.getPrice();
         ItemDataUnit unit = item.getUnit();
         unit.setSource(source);
         OperationResult or = player.getItemManager().addItem(unit);
         if (or.isOk()) {
            String reduceDetail = IngotChangeType.getItemLogDetail(unit.getModelID()) + "," + unit.getCount();
            switch(item.getMoneyType()) {
            case 2:
               PlayerManager.reduceIngot(player, money, IngotChangeType.Mall, reduceDetail);
               break;
            case 3:
            default:
               PlayerManager.reduceMoney(player, money);
               break;
            case 4:
               PlayerManager.reduceBindIngot(player, money, IngotChangeType.Mall, reduceDetail);
               break;
            case 5:
               PlayerManager.reduceRedeemPoints(player, money);
            }

            MallBuy.directlyUseItem(player, item.getModelID(), or, true);
         }

         result = or.getResult();
      }

      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

      this.writeBoolean(result == 1);
      if (result == 1) {
         this.writeDouble((double)id);
      }

      player.writePacket(this);
   }

   private int canBuy(Player player, long itemID) {
      Goods item = ShortcutBuyPanel.getSellItem(itemID);
      if (item == null) {
         return 3002;
      } else {
         int money = item.getPrice();
         switch(item.getMoneyType()) {
         case 1:
            if (!PlayerManager.hasEnoughMoney(player, money)) {
               return 1011;
            }
            break;
         case 2:
            if (player.getIngot() < money) {
               return 1015;
            }
         case 3:
         default:
            break;
         case 4:
            if (player.getBindIngot() < money) {
               return 23004;
            }
            break;
         case 5:
            if (player.getRedeemPoints() < money) {
               return 16707;
            }
         }

         return 1;
      }
   }
}
