package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.rune.RuneForgingData;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemRune;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.List;

public class RequestRuneMosaic extends ReadAndWritePacket {
   int mosaicCount = 0;

   public RequestRuneMosaic(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      Item item = StrengthEquipment.getForgingItem(player, itemID);
      int result = this.canMosaic(player, item);
      this.writeDouble((double)itemID);
      this.writeBoolean(result == 1);
      if (result == 1) {
         RuneForgingData data = RuneForgingData.getData(this.mosaicCount);
         List runes = item.getRunes();
         this.writeByte(RuneForgingData.getMaxcount());

         for(int i = 1; i <= RuneForgingData.getMaxcount(); ++i) {
            int openLevel = RuneForgingData.needStarLevel(i);
            this.writeByte(openLevel);
            ItemRune rune = runes.size() >= i ? (ItemRune)runes.get(i - 1) : null;
            this.writeBoolean(rune != null);
            if (rune != null) {
               Item tmpItem = ItemTools.createItem(rune.getModelID(), 1, 2);
               GetItemStats.writeItem(tmpItem, this);
            }
         }

         this.writeInt(data.getMoney());
         this.writeByte(data.getShowRate() / 1000);
      }

      player.writePacket(this);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   private int canMosaic(Player player, Item item) {
      int result = canMosaic(item);
      if (result != 1) {
         return result;
      } else {
         this.mosaicCount = item.getRunes().size() + 1;
         this.mosaicCount = Math.min(this.mosaicCount, RuneForgingData.getMaxcount());
         return 1;
      }
   }

   public static int canMosaic(Item item) {
      if (item != null && item.getCount() >= 1) {
         if (item.isTimeLimited()) {
            return 5068;
         } else {
            return !RuneForgingData.canForging(item.getItemType()) ? 5013 : 1;
         }
      } else {
         return 3002;
      }
   }
}
