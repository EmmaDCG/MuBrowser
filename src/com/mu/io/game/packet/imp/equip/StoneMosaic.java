package com.mu.io.game.packet.imp.equip;

import com.mu.executor.imp.log.ItemForgingExecutor;
import com.mu.game.model.equip.newStone.StoneDataManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemStone;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.HashSet;
import java.util.Iterator;

public class StoneMosaic extends ReadAndWritePacket {
   public StoneMosaic(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      long stoneID = (long)this.readDouble();
      Item item = StrengthEquipment.getForgingItem(player, itemID);
      Item stoneItem = player.getBackpack().getItemByID(stoneID);
      int result = canMosaic(player, item, stoneItem);
      if (result == 1) {
         result = player.getItemManager().updateItemAddStones(item, stoneItem).getResult();
         if (result == 1) {
            ItemForgingExecutor.logFoging(player, item, item.getStarLevel(), 2, true, 0, 0);
         }
      }

      this.writeDouble((double)itemID);
      this.writeBoolean(result == 1);
      if (result == 1) {
         this.writeByte(item.getStones().size() - 1);
      }

      player.writePacket(this);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   public static int canMosaic(Player player, Item item, Item stoneItem) {
      if (item != null && item.getCount() >= 1) {
         if (stoneItem != null && stoneItem.getCount() >= 1) {
            if (item.isTimeLimited()) {
               return 5068;
            } else if (!StoneDataManager.canMosaicByType(item.getItemType())) {
               return 5020;
            } else if (stoneItem.getModel().getSort() != 12) {
               return 5021;
            } else if (item.getSocket() < 1) {
               return 5022;
            } else if (item.getStones().size() >= item.getSocket()) {
               return 5019;
            } else {
               HashSet stoneTypes = StoneDataManager.getMosaicStoneType(item.getItemType());
               if (!stoneTypes.contains(stoneItem.getItemType())) {
                  return 5048;
               } else {
                  Iterator var5 = item.getStones().iterator();

                  while(var5.hasNext()) {
                     ItemStone stone = (ItemStone)var5.next();
                     ItemModel model = ItemModel.getModel(stone.getModelID());
                     if (model.getItemType() == stoneItem.getItemType()) {
                        return 5049;
                     }
                  }

                  return 1;
               }
            }
         } else {
            return 3002;
         }
      } else {
         return 3002;
      }
   }
}
