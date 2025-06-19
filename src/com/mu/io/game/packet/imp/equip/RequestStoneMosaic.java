package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.equipStat.EquipStat;
import com.mu.game.model.equip.newStone.StoneDataManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemStone;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.stats.ItemModify;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class RequestStoneMosaic extends ReadAndWritePacket {
   public RequestStoneMosaic(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      Item item = StrengthEquipment.getForgingItem(player, itemID);
      int result = canMosaic(item);
      this.writeDouble((double)itemID);
      this.writeBoolean(result == 1);
      if (result == 1) {
         List stones = item.getStones();
         int maxCount = StoneDataManager.getMaxCount();
         this.writeByte(maxCount);

         for(int i = 0; i < maxCount; ++i) {
            boolean open = item.getSocket() > i;
            ItemStone stone = stones.size() > i ? (ItemStone)stones.get(i) : null;
            this.writeBoolean(open);
            this.writeBoolean(stone != null);
            if (stone != null) {
               Item tmpItem = ItemTools.createItem(stone.getModelID(), 1, 2);
               List bList = new ArrayList();
               EquipStat stat = EquipStat.getEquipStat(stone.getEquipStatID());
               bList.add(new ItemModify(stat.getStat(), stat.getValue(), stat.getPriority(), 0));
               tmpItem.setBasisStats(bList);
               bList.clear();
               bList = null;
               GetItemStats.writeItem(tmpItem, this);
               this.writeUTF(StoneDataManager.assemStoneStat(stat));
               tmpItem.destroy();
               tmpItem = null;
            }
         }

         List stoneList = player.getBackpack().getItemsBySort(12);
         List canMosaicList = new ArrayList();
         Iterator var17 = stoneList.iterator();

         Item stone;
         while(var17.hasNext()) {
            stone = (Item)var17.next();
            if (filterStone(item, stone) == 1) {
               canMosaicList.add(stone);
            }
         }

         this.writeShort(canMosaicList.size());
         var17 = canMosaicList.iterator();

         while(var17.hasNext()) {
            stone = (Item)var17.next();
            this.writeDouble((double)stone.getID());
         }

         canMosaicList.clear();
         canMosaicList = null;
         stoneList.clear();
         stoneList = null;
      }

      player.writePacket(this);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   public static int canMosaic(Item item) {
      if (item != null && item.getCount() >= 1) {
         if (item.isTimeLimited()) {
            return 5068;
         } else if (!StoneDataManager.canMosaicByType(item.getItemType())) {
            return 5020;
         } else {
            return item.getShowSockety() < 1 ? 5022 : 1;
         }
      } else {
         return 3002;
      }
   }

   public static int filterStone(Item item, Item stoneItem) {
      HashSet stoneTypes = StoneDataManager.getMosaicStoneType(item.getItemType());
      if (!stoneTypes.contains(stoneItem.getItemType())) {
         return 5048;
      } else {
         Iterator var4 = item.getStones().iterator();

         while(var4.hasNext()) {
            ItemStone stone = (ItemStone)var4.next();
            ItemModel model = ItemModel.getModel(stone.getModelID());
            if (model.getItemType() == stoneItem.getItemType()) {
               return 5049;
            }
         }

         return 1;
      }
   }
}
