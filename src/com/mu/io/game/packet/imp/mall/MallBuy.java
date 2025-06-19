package com.mu.io.game.packet.imp.mall;

import com.mu.config.MessageText;
import com.mu.db.log.IngotChangeType;
import com.mu.game.CenterManager;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.mall.MallConfigManager;
import com.mu.game.model.mall.MallItemData;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.popup.imp.MallIngotPopup;
import com.mu.game.model.vip.effect.VIPEffectType;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class MallBuy extends ReadAndWritePacket {
   public MallBuy(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long id = (long)this.readDouble();
      int count = this.readShort();
      if (count <= 0) {
         CenterManager.gameServerActivieOffChannel(this.getChannel());
      } else {
         buyMallItem(player, id, count, false);
      }
   }

   public static void buyMallItem(Player player, long id, int count, boolean autoUse) {
      MallItemData mid = MallConfigManager.getData(id);
      if (mid == null) {
         SystemMessage.writeMessage(player, 23001);
      } else if (mid.getVipLevel() <= player.getVIPLevel() && (mid.getVipLevel() <= 0 || player.getVIPManager().getEffectBooleanValue(VIPEffectType.VE_13))) {
         int money = mid.getPrice2() * count;
         switch(mid.getPriceType()) {
         case 1:
            if (player.getMoney() < money) {
               SystemMessage.writeMessage(player, 23003);
               return;
            }
            break;
         case 2:
            if (player.getIngot() < money) {
               ShowPopup.open(player, new MallIngotPopup(player.createPopupID()));
               return;
            }
            break;
         case 3:
         default:
            return;
         case 4:
            if (player.getBindIngot() < money) {
               SystemMessage.writeMessage(player, 23004);
               return;
            }
            break;
         case 5:
            if (player.getRedeemPoints() < money) {
               SystemMessage.writeMessage(player, 23005);
               return;
            }
         }

         ItemDataUnit idu = mid.cloneUnit();
         idu.setCount(count);
         OperationResult result = player.getItemManager().addItem(idu);
         if (result.isOk()) {
            String reduceDetail = IngotChangeType.getItemLogDetail(idu.getModelID()) + "," + count;
            switch(mid.getPriceType()) {
            case 1:
               PlayerManager.reduceMoney(player, money);
               break;
            case 2:
               PlayerManager.reduceIngot(player, money, IngotChangeType.Mall, reduceDetail);
            case 3:
            default:
               break;
            case 4:
               PlayerManager.reduceBindIngot(player, money, IngotChangeType.Mall, reduceDetail);
               break;
            case 5:
               PlayerManager.reduceRedeemPoints(player, money);
            }
         } else {
            SystemMessage.writeMessage(player, 23006);
         }

         directlyUseItem(player, idu.getModelID(), result, autoUse);
      } else {
         SystemMessage.writeMessage(player, MessageText.getText(23100), 23100);
      }
   }

   public static void directlyUseItem(Player player, int modelID, OperationResult result, boolean autoUse) {
      if (result.isOk() && autoUse) {
         ItemModel model = ItemModel.getModel(modelID);
         if (!model.isDirectly()) {
            return;
         }

         player.getItemManager().useItem(player.getBackpack().getItemByID(result.getItemID()), 1, true);
      }

   }
}
