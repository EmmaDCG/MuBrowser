package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.forging.ForgingRuleDes;
import com.mu.game.model.equip.upgrade.UpgradeData;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class RequestUpgrade extends ReadAndWritePacket {
   public RequestUpgrade(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      boolean useIngot = this.readBoolean();
      Item item = StrengthEquipment.getForgingItem(player, itemID);
      int result = canUpgrade(player, item);
      if (result == 1) {
         UpgradeData data = UpgradeData.getUpgradeData(item.getModelID());
         Item targetItem = item.cloneItem(2);
         targetItem.reset(data.getTaregtID());
         this.writeBoolean(true);
         GetItemStats.writeItem((Item)data.getShowItems().get(0), this);
         GetItemStats.writeItem(targetItem, this);
         this.writeByte((useIngot ? 100000 : data.getRate()) / 1000);
         this.writeInt(data.getIngot());
         this.writeUTF(ForgingRuleDes.UpdateDes);
      } else {
         this.writeBoolean(false);
      }

      player.writePacket(this);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   public static int canUpgrade(Player player, Item item) {
      int result = canUpgrade(item);
      if (result != 1) {
         return result;
      } else {
         UpgradeData data = UpgradeData.getUpgradeData(item.getModelID());
         if (item.getContainerType() == 0) {
            ItemModel targetModel = ItemModel.getModel(data.getTaregtID());
            if (ItemAction.canUse(targetModel, player) != 1) {
               return 5067;
            }
         }

         return 1;
      }
   }

   public static int canUpgrade(Item item) {
      if (item != null && item.getCount() >= 1) {
         if (item.isTimeLimited()) {
            return 5068;
         } else {
            return UpgradeData.hasUpgrade(item.getModelID()) ? 1 : 5066;
         }
      } else {
         return 3002;
      }
   }
}
