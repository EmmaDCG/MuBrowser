package com.mu.io.game.packet.imp.equip;

import com.mu.db.log.IngotChangeType;
import com.mu.executor.imp.log.ItemForgingExecutor;
import com.mu.game.model.equip.upgrade.UpgradeData;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Rnd;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class UpgradeItem extends ReadAndWritePacket {
   public UpgradeItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      boolean useIngot = this.readBoolean();
      Item item = StrengthEquipment.getForgingItem(player, itemID);
      int result = canUpgrade(player, item, useIngot);
      int type = 2;
      if (result == 1) {
         UpgradeData data = UpgradeData.getUpgradeData(item.getModelID());
         HashMap delMap = new HashMap();
         delMap.putAll(data.getNeedItems());
         result = player.getItemManager().deleteItemByModel(delMap, 21).getResult();
         delMap.clear();
         delMap = null;
         if (result == 1) {
            PlayerManager.reduceMoney(player, data.getMoney());
            if (!useIngot && Rnd.get(1, 100000) > data.getRate()) {
               player.getItemManager().deleteItem(item, 21);
            } else {
               player.getItemManager().upgradeItem(item, data.getTaregtID());
               type = 1;
            }

            if (useIngot) {
               PlayerManager.reduceIngot(player, data.getIngot(), IngotChangeType.UpdateEquip, IngotChangeType.getItemLogDetail(data.getTaregtID()));
            }

            ItemForgingExecutor.logFoging(player, item, item.getStarLevel(), 3, type == 1, 0, 0);
         }
      }

      this.writeDouble((double)itemID);
      this.writeBoolean(type == 1);
      player.writePacket(this);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      } else if (type != 1) {
         SystemMessage.writeMessage(player, 5069);
      }

   }

   public static int canUpgrade(Player player, Item item, boolean useIngot) {
      int result = RequestUpgrade.canUpgrade(item);
      if (result != 1) {
         return result;
      } else if (item.isTimeLimited()) {
         return 5068;
      } else {
         UpgradeData data = UpgradeData.getUpgradeData(item.getModelID());
         if (item.getContainerType() == 0) {
            ItemModel targetModel = ItemModel.getModel(data.getTaregtID());
            if (ItemAction.canUse(targetModel, player) != 1) {
               return 5067;
            }
         }

         Iterator it = data.getNeedItems().entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            if (!player.getBackpack().hasEnoughItem(((Integer)entry.getKey()).intValue(), ((Integer)entry.getValue()).intValue())) {
               return 3001;
            }
         }

         return useIngot && player.getIngot() < data.getIngot() ? 1015 : 1;
      }
   }
}
